
@Component
class Lims1Api: Actor {
    private val logger = LoggerFactory.getLogger(javaClass)
    @Autowired
    public lateinit var request : HttpRequest
    @Autowired
    public  lateinit var client: HttpClient
    @Autowired
    public  lateinit var bodyHandler: BatchBodyHandler<BatchDTO>
    @Autowired
    public  lateinit var batchUpdater: BatchUpdater

    private val om = ObjectMapper()
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        .registerModule(JavaTimeModule())
        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)

    override fun genericType(): Class<SequencingEvent<out Mapper>> {
        return SequencingEvent::class.java
    }
    override fun onNext(event: SequencingEvent<out Mapper>) {
        client.sendAsync(request, bodyHandler)
            .thenAccept { response ->
                val batchToUpdate = response.body().get()!!.filter{ batch ->
                    (batch!!.state == "CREATE" && batch.value.getOrDefault(UUIDEnum.PROGRESS.toUUID(), null) != "1.0" && batch.value.getOrDefault(UUIDEnum.DEVICE.toUUID(), null) == event.device && batch.value.getOrDefault(UUIDEnum.PATH.toUUID(), null) == event.path)
                }.findAny()
                if(batchToUpdate.isPresent){
                    logger.info("Update Batch")
                    updateBatch(batchToUpdate.get(), event)
                } else {
                    logger.info("Create New Batch")
                    createBatch(event)
                }
            }.join()
    }

    private fun updateBatch(oldBatch : BatchDTO, event : SequencingEvent<out Mapper>){
        val batch = batchUpdater.update(oldBatch, event)
        val putRequest = HttpRequest
            .newBuilder()
            .uri(URI.create("http://172.19.210.215/api2/batch/" + batch.template + "/" + batch.idx))
            .PUT(HttpRequest.BodyPublishers.ofString(om.writeValueAsString(batch)))
            .header("Content-Type", "application/json")
            .build()
        val response = client.send(putRequest, HttpResponse.BodyHandlers.ofString())
        if(response.statusCode() != 200)
            logger.error("Updated failed, Server sent response : \n" + response.statusCode() + "\n" + response.body())
    }

    @Synchronized
    private fun createBatch(event: SequencingEvent<out Mapper>){
        val getRequest = HttpRequest
            .newBuilder()
            .uri(URI.create("http://172.19.210.215/api2/batch/" + UUIDEnum.TEMPLATE.uuid + "/max" ))
            .GET()
            .build()
        val batch = BatchDTO()
        batch.value = HashMap<UUID, String>()
        batch.state = "CREATE"
        batch.template = UUIDEnum.TEMPLATE.toUUID()
        batch.idx = client.send(getRequest, HttpResponse.BodyHandlers.ofString()).body().toString().toInt() + 1
        updateBatch(batch, event)
    }
}