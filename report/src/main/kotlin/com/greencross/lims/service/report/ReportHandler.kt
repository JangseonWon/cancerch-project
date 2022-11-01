package com.greencross.lims.service.report

import com.fasterxml.jackson.databind.ObjectMapper
import com.gcgenome.lims.avoid.TestInfo
import com.gcgenome.lims.data.MessageReport
import com.greencross.lims.data.Report_
import com.greencross.lims.entity.ReportFile
import com.greencross.lims.projection.Analysis
import com.greencross.lims.projection.Report
import com.greencross.lims.report.avoid.*
import com.greencross.lims.report.avoid.kokr.AvoidResourceN201KoKr
import com.greencross.lims.report.avoid.kokr.AvoidTemplateN201KoKr
import com.greencross.lims.report.avoid.repository.CancerRepo
import com.greencross.lims.report.builder.LogoType
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.func.Painter
import com.greencross.lims.report.kokr.SectionFooterGenome
import com.greencross.lims.report.kokr.SectionPage
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
    private val cancerRepo: CancerRepo,
    private val fileRepo: ReportFileRepository,
    private val mapper: ReportMapper,
    private val om: ObjectMapper
) {
    private val logger      = LoggerFactory.getLogger("ReportSearch")
    private val publisher   = Sinks.many().unicast().onBackpressureBuffer<MessageReport>()
    private val subscriber  = Sinks.many().multicast().directAllOrNothing<MessageReport>()
    @Transactional
    fun reports(sample: Long, service: String): Flux<Report> {
        return reportDao.findBySampleAndService(sample, service)
    }

    @Transactional
    fun print(sample: Long, service: String, lang: String): Mono<Void> {
        val createTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(LocalDateTime.now().toInstant(OffsetDateTime.now().offset).toEpochMilli()),
            ZoneId.systemDefault()
        )
        val entity =
            com.greencross.lims.entity.Report(sample = sample, service = service, createAt = createTime).apply {
                language = lang
                isPrinted = "PREPARE"
            }
        return reportDao.create(entity).flatMap {
            logger.info("$sample/$service is created.")
            publisher.tryEmitNext(MessageReport(MessageReport.MessageType.CREATE, mapper.toMessageDto(entity)))
            Mono.empty()
        }
    }

    @Transactional
    fun scheduleReports(): Mono<Void> {
        logger.info("CronJob Running: Period 10 sec.")
        return reportDao.findReport().flatMap {
            analysisDao.findById(it.sample, it.service).zipWith(Mono.just(it)).flatMap { zipped ->
                logger.info(zipped.t1.sample.toString()+"/"+zipped.t1.service + " is printing.")
                publisher.tryEmitNext(MessageReport(MessageReport.MessageType.PRINTING, mapper.toMessageDto(zipped.t2)))
                val dto = analysisToAvoidDto(zipped.t1)
                val baos = ByteArrayOutputStream()
                val doc = zipped.t2.language?.let { it1 -> build(it1, dto) }
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
                zipped.t2.apply{
                    this.file = reportFile.id
                    this.name = reportFile.name!!
                    this.size = reportFile.size
                    this.isPrinted="COMPLETED"
                }
                reportDao.merge(zipped.t2).doOnSuccess {
                    logger.info(zipped.t1.sample.toString()+"/"+zipped.t1.service + " is finished.")
                    publisher.tryEmitNext(MessageReport(MessageReport.MessageType.FINISH, mapper.toMessageDto(zipped.t2)))
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
        return Supplier { publisher.asFlux().map(this::messageToString)}
    }
    @Bean("broadcast-printing")
    fun broadcastReports(): Consumer<String> {
        return Consumer { c: String -> subscriber.tryEmitNext(stringToMessage(c))}
    }
    fun subscribe(): Flux<MessageReport> {
        return subscriber.asFlux()
    }
    private fun analysisToAvoidDto(analysis: Analysis): AvoidDto {
        val patient = analysis.patient
        val barcode = analysis.value
        val (customerName, requestNumber) = if (patient.customerName2 != null) Pair(
            patient.customerName2, formatSampleId(
                analysis.remark?.toLongOrNull()
            )
        )
        else Pair(patient.customerName, formatSampleId(analysis.sample))
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

        val avoidDto = AvoidDto(barcode, stringToResult(analysis.result), cancer1)
        avoidDto.barcode = barcode
        avoidDto.patientName = patient.name
        avoidDto.birthDate = patient.birth
        avoidDto.age = age(avoidDto.birthDate, analysis.dateSampling.toLocalDate()).toString()
        avoidDto.sex = sex(patient.sex)
        avoidDto.requestNumber = requestNumber
        avoidDto.collectionDate = analysis.dateSampling.toLocalDate()
        avoidDto.receiptDate = analysis.dateRequest.toLocalDate()
        avoidDto.reportDate = LocalDate.now()
        avoidDto.medicalRecordNumber = analysis.patient.mrn
        avoidDto.barcode = barcode
        avoidDto.medicalInstitution = customerName
        avoidDto.specimenType = analysis.sampleType

        return avoidDto
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

    private fun build(lang: String, dto: AvoidDto): PDDocument? {
        return builder(TestInfo.N201, LogoType.DEPENDENT, dto)?.build()
    }

    private fun builder(test: TestInfo, logo: LogoType, dto: AvoidDto): AvoidPageBuilder<*>? {
        val doc = PDDocument()

        val sign: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionSign(65f)
        val footer: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionFooterGenome()
        val page: Painter<AvoidTemplate<AvoidResource>, AvoidDto>
        return if (TestInfo.N201 == test) {
            var resource = AvoidResourceN201KoKr(doc)
            var template = AvoidTemplateN201KoKr(resource, test)

            page = SectionPage(547f, 65f, resource.fontDefault())

            return AvoidN201(template as AvoidTemplateN201<AvoidResource>, dto, sign, footer, page)
        } else null
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
        else -> CancerRepo.암종.유방암
    }

    private fun cancerToFileName(cancer: String) = when (cancer) {
        "LuC" -> "폐암"
        "Panc" -> "췌장담도암"
        "HCC" -> "간암"
        "colon" -> "대장암"
        "Others" -> "기타암종"
        "ESO" -> "식도암"
        else -> "유방암"
    }

    private fun stringToResult(result: String) = when (result) {
        "GENERAL" -> AvoidDto.Results.GENERAL
        "CONCERN" -> AvoidDto.Results.CONCERN
        else -> AvoidDto.Results.RISK
    }

    private fun formatSampleId(id: Long?): String? {
        if (id == null) return null
        val cast = id.toString()
        return if (cast.length == 15) "${cast.substring(0, 8)}-${cast.substring(8, 11)}-${cast.substring(11)}"
        else cast
    }

    private fun messageToString(msg: MessageReport): String {
        return om.writeValueAsString(msg)
    }
    private fun stringToMessage(str: String): MessageReport {
        return om.readValue(str, MessageReport::class.java)
    }
}