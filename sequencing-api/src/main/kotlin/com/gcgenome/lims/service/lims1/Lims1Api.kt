package com.gcgenome.lims.service.lims1

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.gcgenome.lims.entity.Worklist
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class Lims1Api(private val worklistToBatch: WorklistToBatch) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val client: HttpClient = HttpClient.newHttpClient()
    private val om = ObjectMapper()
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        .registerModule(JavaTimeModule())
        .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
    @Synchronized
    fun create(worklist: List<Worklist>): Mono<Int> {
        val getRequest = HttpRequest.newBuilder().uri(URI.create("http://172.19.210.215/api2/batch/" + UUIDEnum.TEMPLATE.uuid + "/max" )).GET().build()
        val idx =  client.send(getRequest, HttpResponse.BodyHandlers.ofString()).body().toString().toInt() + 1
        return worklistToBatch.map(idx, worklist).flatMap { batch->
            val putRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://172.19.210.215/api2/batch/" + batch.template + "/" + batch.idx))
                .PUT(HttpRequest.BodyPublishers.ofString(om.writeValueAsString(batch)))
                .header("Content-Type", "application/json")
                .build()
            val response = client.send(putRequest, HttpResponse.BodyHandlers.ofString())
            if(response.statusCode() != 200) logger.error("Updated failed, Server sent response : \n" + response.statusCode() + "\n" + response.body())
            for(analysis in batch.analysis) {
                val putRequest2 = HttpRequest
                    .newBuilder()
                    .uri(URI.create("http://172.19.210.215/api2/batch/" + batch.template + "/" + batch.idx + "/analysis/" + analysis.row))
                    .PUT(HttpRequest.BodyPublishers.ofString(om.writeValueAsString(analysis)))
                    .header("Content-Type", "application/json")
                    .build()
                println(om.writeValueAsString(analysis))
                val response2 = client.send(putRequest2, HttpResponse.BodyHandlers.ofString())
                if(response2.statusCode() != 200) logger.error("Updated failed, Server sent response : \n" + response2.statusCode() + "\n" + response2.body())
            }
            Mono.just(response.statusCode())
        }
    }
}