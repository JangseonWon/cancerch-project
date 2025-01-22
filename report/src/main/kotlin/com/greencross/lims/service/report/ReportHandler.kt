package com.greencross.lims.service.report

import com.fasterxml.jackson.databind.ObjectMapper
import com.gcgenome.lims.avoid.TestInfo
import com.gcgenome.lims.data.MessageReport
import com.gcgenome.report.versions.log.ReactiveLogService
import com.gcgenome.report.versions.report.ReactiveReportVersionService
import com.greencross.lims.data.Report_
import com.greencross.lims.entity.ReportFile
import com.greencross.lims.projection.Analysis
import com.greencross.lims.report.avoid.*
import com.greencross.lims.report.avoid.kokr.AvoidResourceN201KoKr
import com.greencross.lims.report.avoid.kokr.AvoidTemplateN201KoKr
import com.greencross.lims.report.avoid.repository.CancerRepo
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.cancerch.*
import com.greencross.lims.report.cancerch.enus.CancerchResourceON203EnUs
import com.greencross.lims.report.cancerch.enus.CancerchResourceON256EnUs
import com.greencross.lims.report.cancerch.enus.CancerchTemplateON203EnUs
import com.greencross.lims.report.cancerch.enus.CancerchTemplateON256EnUs
import com.greencross.lims.report.cancerch.kokr.CancerchResourceN203KoKr
import com.greencross.lims.report.cancerch.kokr.CancerchResourceN256KoKr
import com.greencross.lims.report.cancerch.kokr.CancerchTemplateN203KoKr
import com.greencross.lims.report.cancerch.kokr.CancerchTemplateN256KoKr
import com.greencross.lims.report.cancerch.repository.CancerchRepo
import com.greencross.lims.report.enus.SectionFooterEngGenomeNotColorBar
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.kokr.*
import com.greencross.lims.report.kokr.SectionSign
import com.greencross.lims.service.analysis.AnalysisDao
import com.greencross.lims.service.reportfile.ReportFileRepository
import org.apache.pdfbox.pdmodel.PDDocument
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

@Service
class ReportHandler(
    private val analysisDao: AnalysisDao,
    private val reportDao: ReportDao,
    private val fileRepo: ReportFileRepository,
    private val mapper: ReportMapper,
    private val logService: ReactiveLogService,
    private val reportVersionService: ReactiveReportVersionService,
    private val om: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger("ReportSearch")
    private val publisher = Sinks.many().unicast().onBackpressureBuffer<MessageReport>()
    private val subscriber = Sinks.many().multicast().directAllOrNothing<MessageReport>()
    fun getLogs(sample: Long, service: String) = logService.getLogs(sample, service)
    fun getReports(sample: Long, service: String) = reportVersionService.getReportLogPdf(sample, service)

    @Transactional
    fun print(sample: Long, service: String, batch: String, row: Long, lang: String, description: String): Mono<Void> {
        return analysisDao.findById(sample, service, batch, row)
            .map { mapper.createReportEntity(it, batch, row, lang, description) }
            .flatMap(reportDao::create)
            .flatMap {
                logger.info("$sample/$service is scheduled.")
                publisher.tryEmitNext(MessageReport(MessageReport.MessageType.CREATE, mapper.toMessageDto(it)))
                Mono.empty()
            }
    }

    @Transactional
    fun scheduleReports(): Mono<Void> {
        logger.info("CronJob Running: Period 1 Min.")
        return reportDao.findReport().flatMap {
            analysisDao.findById(it.sample, it.service, it.batch, it.row).zipWith(Mono.just(it)).flatMap { zipped ->
                logger.info(zipped.t1.sample.toString() + "/" + zipped.t1.service + " is printing.")
                publisher.tryEmitNext(MessageReport(MessageReport.MessageType.PRINTING, mapper.toMessageDto(zipped.t2)))
                val baos = ByteArrayOutputStream()
                val doc = when (zipped.t1.service) {
                    "N201" -> build(analysisToAvoidDto(zipped.t1))
                    "N256", "ON256" -> build(analysisToCancerchDto(zipped.t1), "gangbuk", zipped.t1.service)
                    else -> build(
                        analysisToCancerchDto(zipped.t1), if (zipped.t1.patient.customerName == "Gclabs") "labs"
                        else "genome", zipped.t1.service
                    )
                }

                val createTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(LocalDateTime.now().toInstant(OffsetDateTime.now().offset).toEpochMilli()),
                    ZoneId.systemDefault()
                )
                doc?.save(baos)
                val fileName =
                    "${zipped.t1.sample}_${zipped.t1.service}_${
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"))
                    }.pdf"

                val reportFile = ReportFile(UUID.randomUUID()).apply {
                    this.createTime = createTime
                    this.data = ByteBuffer.wrap(baos.toByteArray())
                    this.extension = "pdf"
                    this.name = fileName
                    this.size = baos.toByteArray().size.toLong()
                }
                baos.close()

                fileRepo.save(reportFile)

                zipped.t2.apply {
                    this.file = reportFile.id
                    this.name = reportFile.name!!
                    this.size = reportFile.size
                    this.isPrinted = "COMPLETED"
                }

                reportDao.merge(zipped.t2).doOnSuccess {
                    logger.info(zipped.t1.sample.toString() + "/" + zipped.t1.service + " is finished.")
                    publisher.tryEmitNext(
                        MessageReport(
                            MessageReport.MessageType.FINISH,
                            mapper.toMessageDto(zipped.t2)
                        )
                    )
                }
            }
        }.then(Mono.empty())
    }

    @Transactional
    fun preview(sample: Long, service: String, createAt: Long): Mono<ByteArray> {
        return reportDao.findForCassandraReport(
            sample,
            service,
            LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId())
        )
            .map {
                fileRepo.findById(it.file)
            }
            .map { it.get().data!!.array() }
    }

    @Transactional
    fun works(): Flux<Report_> {
        return reportDao.findRequestQueue().map(mapper::toDto)
    }

    @Bean("publish-printing")
    fun publishReports(): Supplier<Flux<String>> {
        return Supplier { publisher.asFlux().map(this::messageToString) }
    }

    @Bean("broadcast-printing")
    fun broadcastReports(): Consumer<String> {
        return Consumer { c: String -> subscriber.tryEmitNext(stringToMessage(c)) }
    }

    fun subscribe(): Flux<MessageReport> = subscriber.asFlux()

    private fun analysisToAvoidDto(analysis: Analysis): AvoidDto {
        val cancerRepo = CancerRepo()
        val patient = analysis.patient
        val barcode = analysis.value

        val (customerName, requestNumber) =
            if (patient.customerName == "Gclabs") Pair(patient.customerName2, formatSampleId(analysis.remark!!))
            else if(patient.customerName2 != null) Pair(patient.customerName2, analysis.remark ?: formatSampleId(analysis.sample.toString()))
            else Pair(patient.customerName, formatSampleId(analysis.sample.toString()))
        val result = if (sex(patient.sex) == Sex.M) analysis.too5Pred else analysis.too6Pred
        val cancer1 = when (stringToEnum(analysis.result)) {
            CancerRepo.결과.GENERAL -> AvoidDto.Cancer()
            CancerRepo.결과.CONCERN -> AvoidDto.Cancer("기타암종")
            else -> AvoidDto.Cancer(
                cancerToFileName(result),
                cancerRepo.findPPVbyAgeAndCancerAndSex(
                    stringToCancer(result), age(patient.birth, analysis.dateSampling.toLocalDate()), sex(patient.sex)
                )!!,
                cancerRepo.findASRbyAgeAndCancerAndSex(
                    stringToCancer(result), age(patient.birth, analysis.dateSampling.toLocalDate()), sex(patient.sex)
                )!!,
                null,
                analysis.comment ?: "comment"
            )
        }

        val avoidDto = AvoidDto(barcode, stringToAvoidResult(analysis.result), cancer1)
        avoidDto.barcode = barcode
        avoidDto.patientName = patient.name
        avoidDto.birthDate = patient.birth
        avoidDto.age = age(avoidDto.birthDate, analysis.dateSampling.toLocalDate()).toString()
        avoidDto.sex = sex(patient.sex)
        avoidDto.requestNumber = requestNumber ?: ""
        avoidDto.collectionDate = analysis.dateSampling.toLocalDate()
        avoidDto.receiptDate = analysis.dateRequest.toLocalDate()
        avoidDto.reportDate = LocalDate.now()
        avoidDto.medicalRecordNumber = analysis.patient.mrn ?: ""
        avoidDto.barcode = barcode
        avoidDto.medicalInstitution = customerName ?: ""
        avoidDto.specimenType = analysis.sampleType

        return avoidDto
    }

    private fun analysisToCancerchDto(analysis: Analysis): CancerchDto {
        val cancerRepo = CancerchRepo()
        val patient = analysis.patient
        val barcode = analysis.value
        val (customerName, requestNumber) =
            if (patient.customerName == "Gclabs") Pair(patient.customerName2, formatSampleId(analysis.remark!!))
            else if(patient.customerName2 != null) Pair(patient.customerName2, analysis.remark ?: formatSampleId(analysis.sample.toString()))
            else Pair(patient.customerName, formatSampleId(analysis.sample.toString()))
        val result = if (sex(patient.sex) == Sex.M) analysis.too5Pred else analysis.too6Pred
        val cancer1 = when (stringToEnum(analysis.result)) {
            CancerRepo.결과.GENERAL -> CancerchDto.Cancer(comment = analysis.comment ?: "")
            CancerRepo.결과.CONCERN -> CancerchDto.Cancer("기타암종", comment = analysis.comment ?: "")
            else -> CancerchDto.Cancer(
                cancerToFileName(result),
                cancerRepo.findPPVbyAgeAndCancerAndSex(
                    stringToCancer2(result), age(patient.birth, analysis.dateSampling.toLocalDate()), sex(patient.sex)
                )!!,
                cancerRepo.findASRbyAgeAndCancerAndSex(
                    stringToCancer2(result), age(patient.birth, analysis.dateSampling.toLocalDate()), sex(patient.sex)
                )!!,
                null,
                analysis.comment ?: ""
            )
        }

        val cancerchDto = CancerchDto(barcode, stringToCancerchResult(analysis.result), cancer1)
        cancerchDto.barcode = barcode
        cancerchDto.patientName = patient.name
        cancerchDto.birthDate = patient.birth
        cancerchDto.age = age(cancerchDto.birthDate, analysis.dateSampling.toLocalDate()).toString()
        cancerchDto.sex = sex(patient.sex)
        cancerchDto.requestNumber = requestNumber ?: ""
        cancerchDto.collectionDate = analysis.dateSampling.toLocalDate()
        cancerchDto.receiptDate = analysis.dateRequest.toLocalDate()
        cancerchDto.reportDate = LocalDate.now()
        cancerchDto.medicalRecordNumber = analysis.patient.mrn ?: ""
        cancerchDto.barcode = barcode
        cancerchDto.medicalInstitution = customerName ?: ""
        cancerchDto.specimenType = analysis.sampleType

        return cancerchDto
    }

    private fun age(birth: LocalDate?, sampling: LocalDate?): Int {
        if (birth == null) return 0
        return if (sampling == null) {
            val americanAge = LocalDateTime.now().minusYears(birth.year.toLong()).year.toLong()
            if (birth.plusYears(americanAge).isAfter(LocalDate.now())) americanAge.toInt() - 1
            else americanAge.toInt()
        } else {
            val americanAge = sampling.minusYears(birth.year.toLong()).year.toLong()
            if (birth.plusYears(americanAge).isAfter(sampling)) americanAge.toInt() - 1
            else americanAge.toInt()
        }
    }

    private fun sex(sex: String): Sex {
        return Sex.valueOf(sex)
    }

    private fun build(dto: AvoidDto): PDDocument? {
        return builder(dto)?.build()
    }

    private fun build(dto: CancerchDto, type: String, service: String): PDDocument? {
        return when (type) {
            "genome" -> builderGenome(service, dto)?.build()
            "gangbuk" -> buildGangbuk(service, dto)?.build()
            else -> builderLabsGenome(service, dto)?.build()
        }
    }

    private fun builder(dto: AvoidDto): AvoidPageBuilder<*>? {
        val doc = PDDocument()
        val sign: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionSign(65f)
        val footer: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionFooterGenome()
        val page: Painter<AvoidTemplate<AvoidResource>, AvoidDto>
        val resource = AvoidResourceN201KoKr(doc)
        val template = AvoidTemplateN201KoKr(resource, TestInfo.N201)

        page = SectionPage(547f, 65f, resource.fontDefault())

        return AvoidN201(template as AvoidTemplateN201<AvoidResource>, dto, sign, footer, page)
    }

    private fun buildGangbuk(service: String, dto: CancerchDto): CancerchPageBuilder<*>? {
        val doc = PDDocument()

        val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionSign(65f)
        val page: Painter<CancerchTemplate<CancerchResource>, CancerchDto>

        when (service) {
            TestInfo.N256.code() -> {
                val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionFooterGenomeNotColorBar()
                val resource = CancerchResourceN256KoKr(doc)
                val template = CancerchTemplateN256KoKr(resource, TestInfo.N256)
                page = SectionPage(547f, 65f, resource.fontDefault())
                return CancerchGangbukKoKr(template as CancerchTemplateN256<CancerchResource>, dto, sign, footer, page)
            }

            TestInfo.ON256.code() -> {
                val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionFooterGenomeNotColorBar()
                val resource = CancerchResourceON256EnUs(doc)
                val template = CancerchTemplateON256EnUs(resource, TestInfo.ON256)
                page = SectionPage(547f, 65f, resource.fontDefault())

                return CancerchGangbukEnUs(template as CancerchTemplateON256<CancerchResource>, dto, sign, footer, page)
            }

            else -> {
                logger.warn("ERROR : Unknown Service Code : " + service)
                return null
            }
        }
    }

    private fun builderGenome(service: String, dto: CancerchDto): CancerchPageBuilder<*>? {
        val doc = PDDocument()

        val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionSign(65f)
        val page: Painter<CancerchTemplate<CancerchResource>, CancerchDto>

        if (TestInfo.N203.code() == service || TestInfo.N204.code() == service || TestInfo.N205.code() == service || TestInfo.N206.code() == service) {
            val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionFooterGenomeNotColorBar()
            val resource = CancerchResourceN203KoKr(doc)
            val template = CancerchTemplateN203KoKr(resource, TestInfo.N203)
            page = SectionPage(547f, 65f, resource.fontDefault())

            return CancerchN203(template as CancerchTemplateN203<CancerchResource>, dto, sign, footer, page)
        } else if (TestInfo.ON203.code() == service) {
            val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionFooterEngGenomeNotColorBar()
            val resource = CancerchResourceON203EnUs(doc)
            val template = CancerchTemplateON203EnUs(resource, TestInfo.ON203)
            page = SectionPage(547f, 65f, resource.fontDefault())

            return CancerchON203(template as CancerchTemplateON203<CancerchResource>, dto, sign, footer, page)
        } else {
            logger.warn("ERROR : Unknown Service Code : " + service)
            return null
        }
    }

    private fun builderLabsGenome(service: String, dto: CancerchDto): CancerchPageBuilder<*>? {
        val doc = PDDocument()
        val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionSign(65f)
        val page: Painter<CancerchTemplate<CancerchResource>, CancerchDto>

        if (TestInfo.N203.code() == service || TestInfo.N204.code() == service || TestInfo.N205.code() == service || TestInfo.N206.code() == service) {
            val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionFooterGenomeLabsNotColorBar()
            val resource = CancerchResourceN203KoKr(doc)
            val template = CancerchTemplateN203KoKr(resource, TestInfo.N203)
            page = SectionPage(547f, 65f, resource.fontDefault())

            return CancerchN203(template as CancerchTemplateN203<CancerchResource>, dto, sign, footer, page)
        } else if (TestInfo.ON203.code() == service) {
            val resource = CancerchResourceON203EnUs(doc)
            val template = CancerchTemplateON203EnUs(resource, TestInfo.ON203)
            val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionFooterEngGenomeNotColorBar()
            page = SectionPage(547f, 65f, resource.fontDefault())
            return CancerchON203(template as CancerchTemplateON203<CancerchResource>, dto, sign, footer, page)
        } else {
            logger.warn("ERROR : Unknown Service Code : " + service)
            return null
        }
    }

    private fun stringToEnum(result: String) = when (result) {
        "GENERAL" -> CancerRepo.결과.GENERAL
        "CONCERN" -> CancerRepo.결과.CONCERN
        else -> CancerRepo.결과.RISK
    }

    private fun stringToCancer(result: String) = when (result) {
        "LuC" -> CancerRepo.암종.폐암
        "Panc" -> CancerRepo.암종.췌장담도암
        "HCC" -> CancerRepo.암종.간암
        "colon" -> CancerRepo.암종.대장암
        "Others" -> CancerRepo.암종.기타암종
        "ESO" -> CancerRepo.암종.식도암
        "OV" -> CancerRepo.암종.난소암
        else -> CancerRepo.암종.유방암
    }

    private fun stringToCancer2(result: String) = when (result) {
        "LuC" -> CancerchRepo.암종.폐암
        "Panc" -> CancerchRepo.암종.췌장담도암
        "HCC" -> CancerchRepo.암종.간암
        "colon" -> CancerchRepo.암종.대장암
        "Others" -> CancerchRepo.암종.기타암종
        "ESO" -> CancerchRepo.암종.식도암
        "OV" -> CancerchRepo.암종.난소암
        else -> CancerchRepo.암종.유방암
    }

    private fun cancerToFileName(cancer: String) = when (cancer) {
        "LuC" -> "폐암"
        "Panc" -> "췌장담도암"
        "HCC" -> "간암"
        "colon" -> "대장암"
        "Others" -> "기타암종"
        "ESO" -> "식도암"
        "OV" -> "난소암"
        else -> "유방암"
    }

    private fun stringToAvoidResult(result: String): AvoidDto.Results = when (result) {
        "GENERAL" -> AvoidDto.Results.GENERAL
        "CONCERN" -> AvoidDto.Results.CONCERN
        else -> AvoidDto.Results.RISK
    }

    private fun stringToCancerchResult(result: String): CancerchDto.Results = when (result) {
        "GENERAL" -> CancerchDto.Results.GENERAL
        "CONCERN" -> CancerchDto.Results.CONCERN
        else -> CancerchDto.Results.RISK
    }

    private fun formatSampleId(id: String): String? {
        return if (id.length == 15) "${id.substring(0, 8)}-${id.substring(8, 11)}-${id.substring(11)}"
        else id
    }

    private fun messageToString(msg: MessageReport): String {
        return om.writeValueAsString(msg)
    }

    private fun stringToMessage(str: String): MessageReport {
        return om.readValue(str, MessageReport::class.java)
    }
}
