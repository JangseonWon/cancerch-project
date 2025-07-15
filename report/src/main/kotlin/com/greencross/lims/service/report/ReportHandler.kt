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
import com.greencross.lims.report.ON204.*
import com.greencross.lims.report.ON204.enus.resource.DNACXResourceON204EnUs
import com.greencross.lims.report.ON204.enus.template.DNACXTemplateON204EnUs
import com.greencross.lims.report.ON204.jajp.resource.DNACXResourceON204JaJp
import com.greencross.lims.report.ON204.jajp.template.DNACXTemplateON204JaJp
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import com.greencross.lims.report.ON204.template.DNACXTemplateON204
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.DNACTON206EnUs
import com.greencross.lims.report.ON206.DNACTON206JaJp
import com.greencross.lims.report.ON206.DNACTPageBuilder
import com.greencross.lims.report.ON206.enus.resource.DNACTResourceON206EnUs
import com.greencross.lims.report.ON206.enus.template.DNACTTemplateON206EnUs
import com.greencross.lims.report.ON206.jajp.resource.DNACTResourceON206JaJp
import com.greencross.lims.report.ON206.jajp.template.DNACTTemplateON206JaJp
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import com.greencross.lims.report.ON206.template.DNACTTemplateON206
import com.greencross.lims.report.enus.SectionFooterEngGenomeLabsNotColorBar
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
import reactor.core.scheduler.Schedulers
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

    private fun createReport(analysisList: List<Analysis>): ReportFile {
        val baos = ByteArrayOutputStream()
        val targetAnalysis = analysisList.first()
        val doc = when (targetAnalysis.service) {
            //AVOID 검사 분기
            "N201" -> build(analysisToAvoidDto(targetAnalysis))

            //강북삼성 종양DNA검사 분기
            "N256", "J001", "J002" -> build(
                analysisToKoKrCancerchDto(targetAnalysis),
                "gangbuk",
                targetAnalysis.service
            )

            "ON256" -> build(analysisToEnUsCancerchDto(targetAnalysis), "gangbuk", targetAnalysis.service)

            //캔서치검사 분기
            "ON203" -> build(
                analysisToEnUsCancerchDto(targetAnalysis),
                if (targetAnalysis.patient.customerName == "Gclabs") "labs" else "genome", targetAnalysis.service
            )

            "N203", "N204", "N205", "N206", "J024" -> build(
                analysisToKoKrCancerchDto(targetAnalysis),
                if (targetAnalysis.patient.customerName == "Gclabs") "labs" else "genome", targetAnalysis.service
            )

            "ON204" -> build(analysisToDNACXDto(targetAnalysis))
            "ON206" -> build(analysisToDNACTDto(analysisList))
            else -> throw Exception("등록되지 않은 검사코드 : " + targetAnalysis.sample + "/" + targetAnalysis.service)
        }
        val createTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(LocalDateTime.now().toInstant(OffsetDateTime.now().offset).toEpochMilli()),
            ZoneId.systemDefault()
        )
        doc?.save(baos)
        val fileName = "${targetAnalysis.sample}_${targetAnalysis.service}_${
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
        return reportFile
    }

    @Transactional
    fun scheduleReports(): Mono<Long> {
        return reportDao.findReport()
            .doOnNext {
                logger.info(it.sample.toString() + "/" + it.service + " is printing." + "(batch : "+it.batch+" / row : "+it.row)
                publisher.tryEmitNext(MessageReport(MessageReport.MessageType.PRINTING, mapper.toMessageDto(it)))
            }
            .flatMapSequential({ report ->
                analysisDao.findById(report.sample, report.service, report.batch, report.row)
                    .flatMap { analysis ->
                        analysisDao.findOneOrManyBy(analysis)
                            .flatMap { pastList ->
                                Mono.fromCallable {
                                    val reportFile = createReport(pastList)
                                    fileRepo.save(reportFile)   // 동기적으로 ReportFile 반환
                                }.subscribeOn(Schedulers.boundedElastic()) // 블로킹 작업 격리
                            }
                            .flatMap { savedFile ->
                                report.apply {
                                    file = savedFile.id
                                    name = savedFile.name!!
                                    size = savedFile.size
                                    isPrinted = "COMPLETED"
                                }
                                reportDao.merge(report)  // Mono<Report>
                            }
                            .doOnSuccess { finishedReport ->
                                logger.info("${finishedReport.sample}/${finishedReport.service} is finished.")
                                publisher.tryEmitNext(
                                    MessageReport(
                                        MessageReport.MessageType.FINISH,
                                        mapper.toMessageDto(finishedReport)
                                    )
                                )
                            }
                    }
                    .thenReturn(1L)
            }, 1)
            .reduce(0L) { acc, item -> acc + item }
    }

    @Transactional
    fun preview(sample: Long, service: String, createAt: Long): Mono<ByteArray> {
        return reportDao.findForCassandraReport(
            sample,
            service,
            LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId())
        )
            .map { fileRepo.findById(it.file) }
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

    private fun analysisToDNACTDto(analysisList: List<Analysis>): DNACTDto {
        val targetAnalysis = analysisList.first()
        val patient = targetAnalysis.patient
        val barcode = targetAnalysis.value
        val customerName = if (patient.customerName == "GC Lymphotec") patient.customerName2 else patient.customerName
        val result = analysisList.map { analysis ->
            DNACTDto.SummaryOfResult(
                date                = analysis.dateRequest.toLocalDate(),
                cancer              = analysis.clinicalCancer ?:                                                                  throw Exception("${analysis.sample}의 환자 암종이 기재되지 않았습니다."),
                cfDNAConcentration  = if(analysis.cfDnaConcentration != null) analysis.cfDnaConcentration.toFloat()          else throw Exception("${analysis.sample}의 환자 cfDnaConcentration가 기재되지 않았습니다."),
                genomicInstability  = if(analysis.iscore != null) analysis.iscore.toFloat()                                  else throw Exception("${analysis.sample}의 환자 iscore가 기재되지 않았습니다."),
                covScore            = if(analysis.covBc != null) analysis.covBc.toFloat()                                    else throw Exception("${analysis.sample}의 환자 covBc가 기재되지 않았습니다."),
                femsScore           = if(analysis.femsBc != null) analysis.femsBc.toFloat()                                  else throw Exception("${analysis.sample}의 환자 femsBc가 기재되지 않았습니다.")
            ).apply {
                FEMSPath               = if(stringToCTEnum(analysis.result) != DNACTDto.Risk.NOT_DETECTED) analysis.femsPath?:    throw Exception("${analysis.sample}의 환자가 음성이 아님에도 fems 이미지 데이터가 입력되지 않았습니다.") else ""
                GenomicPath            = if(stringToCTEnum(analysis.result) != DNACTDto.Risk.NOT_DETECTED) analysis.iscorePath ?: throw Exception("${analysis.sample}의 환자가 음성이 아님에도 iscore 이미지 데이터가 입력되지 않았습니다.") else ""
            }
        }
        val dto = DNACTDto(
            barcode,
            stringToCTEnum(targetAnalysis.result),
            result,
            targetAnalysis.language ?: "ja-jp"
        )

        return dto.apply {
            this.barcode = barcode
            this.patientName = patient.name
            this.birthDate = patient.birth
            this.age = age(patient.birth, targetAnalysis.dateSampling.toLocalDate()).toString()
            this.sex = sex(patient.sex)
            this.requestNumber = formatSampleId(targetAnalysis.sample.toString())
            this.collectionDate = targetAnalysis.dateSampling.toLocalDate()
            this.receiptDate = targetAnalysis.dateRequest.toLocalDate()
            this.reportDate = LocalDate.now()
            this.medicalRecordNumber = targetAnalysis.patient.mrn ?: "-"
            this.barcode = barcode
            this.medicalInstitution = customerName ?: "-"
            this.specimenType = targetAnalysis.sampleType
            this.comment = targetAnalysis.comment ?: ""
        }
    }

    private fun analysisToDNACXDto(analysis: Analysis): DNACXDto {
        val patient = analysis.patient
        val barcode = analysis.value
        val customerName = if (patient.customerName == "GC Lymphotec") patient.customerName2 else patient.customerName
        val dto = DNACXDto(
            barcode,
            stringToCXEnum(analysis.result),
            DNACXDto.SummaryOfResult(
                cancerToName(if (analysis.patient.sex == "F") analysis.too6Pred else analysis.too5Pred),
                analysis.femsCovBc ?: throw Exception("fems_cov_bc 값이 없습니다 ${analysis.sample} / ${analysis.service}"),
                analysis.covBc ?: throw Exception("cov_bc 값이 없습니다 ${analysis.sample} / ${analysis.service}"),
                analysis.femsBc ?: throw Exception("fems_bc 값이 없습니다 ${analysis.sample} / ${analysis.service}"),
                analysis.cfDnaConcentration
                    ?: throw Exception("cfDNA 값이 없습니다 ${analysis.sample} / ${analysis.service}"),
                analysis.iscore ?: throw Exception("iscore 값이 없습니다 ${analysis.sample} / ${analysis.service}"),
            ), if (analysis.language.isNullOrEmpty()) "ja-jp" else analysis.language
        )
        val age = age(dto.birthDate, dto.collectionDate)
        dto.age = age.toString()
        val (cutoff95, cutoff99) = calculateSignalScoreCutOff(age)
        dto.result.signalScore95CutOff = cutoff95
        dto.result.signalScore99CutOff = cutoff99

        return dto.apply {
            this.barcode = barcode
            this.patientName = patient.name
            this.birthDate = patient.birth
            this.age = age(patient.birth, analysis.dateSampling.toLocalDate()).toString()
            this.sex = sex(patient.sex)
            this.requestNumber = formatSampleId(analysis.sample.toString())
            this.collectionDate = analysis.dateSampling.toLocalDate()
            this.receiptDate = analysis.dateRequest.toLocalDate()
            this.reportDate = LocalDate.now()
            this.medicalRecordNumber = analysis.patient.mrn ?: ""
            this.barcode = barcode
            this.medicalInstitution = customerName ?: ""
            this.specimenType = analysis.sampleType
            this.comment = analysis.comment ?: ""
        }
    }

    private fun calculateSignalScoreCutOff(age: Int): Pair<Double, Double> {
        return if (age <= 59) Pair(0.379, 0.614)
        else if (age in 60..69) Pair(0.501, 0.678)
        else Pair(0.581, 0.793)
    }

    private fun analysisToAvoidDto(analysis: Analysis): AvoidDto {
        val cancerRepo = CancerRepo()
        val patient = analysis.patient
        val barcode = analysis.value

        val (customerName, requestNumber) =
            if (patient.customerName == "Gclabs") Pair(patient.customerName2, formatSampleId(analysis.remark!!))
            else if (patient.customerName2 != null) Pair(
                patient.customerName2,
                analysis.remark ?: formatSampleId(analysis.sample.toString())
            )
            else Pair(patient.customerName, formatSampleId(analysis.sample.toString()))
        val result = if (sex(patient.sex) == Sex.M) analysis.too5Pred else analysis.too6Pred
        val cancer1 = when (stringToEnum(analysis.result)) {
            CancerRepo.결과.GENERAL -> AvoidDto.Cancer()
            CancerRepo.결과.CONCERN -> AvoidDto.Cancer("기타암종")
            else -> AvoidDto.Cancer(
                cancerToName(result),
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

    private fun analysisToKoKrCancerchDto(analysis: Analysis): CancerchDto {
        val cancerRepo = CancerchRepo()
        val patient = analysis.patient
        val barcode = analysis.value
        val customerName = when {
            patient.customerName2 != null -> patient.customerName2
            else -> patient.customerName
        }
        val requestNumber = when {
            analysis.remark != null -> if (patient.customerName2 != null && patient.customerName == "Gclabs") formatSampleId(
                analysis.remark
            ) else analysis.remark

            else -> formatSampleId(analysis.sample.toString())
        }
        val result = if (sex(patient.sex) == Sex.M) analysis.too5Pred else analysis.too6Pred
        val cancer1 = when (stringToEnum(analysis.result)) {
            CancerRepo.결과.GENERAL -> CancerchDto.Cancer(comment = analysis.comment ?: "")
            CancerRepo.결과.CONCERN -> CancerchDto.Cancer("기타암종", comment = analysis.comment ?: "")
            else -> CancerchDto.Cancer(
                cancerToName(result),
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
        cancerchDto.medicalInstitution = customerName
        cancerchDto.specimenType = analysis.sampleType

        return cancerchDto
    }

    private fun analysisToEnUsCancerchDto(analysis: Analysis): CancerchDto {
        val cancerRepo = CancerchRepo()
        val patient = analysis.patient
        val barcode = analysis.value
        val customerName = when {
            analysis.ward != null -> analysis.ward
            patient.customerName2 != null -> patient.customerName2
            else -> patient.customerName
        }
        val requestNumber = when {
            analysis.remark != null -> if (patient.customerName2 != null && patient.customerName == "Gclabs") formatSampleId(
                analysis.remark
            ) else analysis.remark

            else -> formatSampleId(analysis.sample.toString())
        }
        val result = if (sex(patient.sex) == Sex.M) analysis.too5Pred else analysis.too6Pred
        val cancer1 = when (stringToEnum(analysis.result)) {
            CancerRepo.결과.GENERAL -> CancerchDto.Cancer(comment = analysis.comment ?: "")
            CancerRepo.결과.CONCERN -> CancerchDto.Cancer("기타암종", comment = analysis.comment ?: "")
            else -> CancerchDto.Cancer(
                cancerToName(result),
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
        cancerchDto.medicalInstitution = customerName
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

    private fun build(dto: DNACXDto): PDDocument {
        return builderDNACX(dto).build()
    }

    //
    private fun build(dto: DNACTDto): PDDocument {
        return builderDNACT(dto).build()
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
        val resource = AvoidResourceN201KoKr(doc)
        val template = AvoidTemplateN201KoKr(resource, TestInfo.N201)
        val sign: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionSign(65f)
        val footer: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionFooterGenome()
        val page: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionPage(547f, 65f, resource.fontDefault())

        return AvoidN201(template as AvoidTemplateN201<AvoidResource>, dto, sign, footer, page)
    }

    private fun buildGangbuk(service: String, dto: CancerchDto): CancerchPageBuilder<*>? {
        val doc = PDDocument()
        val page: Painter<CancerchTemplate<CancerchResource>, CancerchDto>

        when (service) {
            TestInfo.N256.code(), TestInfo.J001.code(), TestInfo.J002.code() -> {
                val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionSign(65f)
                val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionFooterGenomeNotColorBar()
                val resource = CancerchResourceN256KoKr(doc)
                val template = CancerchTemplateN256KoKr(resource, TestInfo.N256)
                page = SectionPage(547f, 65f, resource.fontDefault())
                return CancerchGangbukKoKr(template as CancerchTemplateN256<CancerchResource>, dto, sign, footer, page)
            }

            TestInfo.ON256.code() -> {
                val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto> =
                    com.greencross.lims.report.enus.SectionSign(65f)
                val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> =
                    SectionFooterEngGenomeNotColorBar()
                val resource = CancerchResourceON256EnUs(doc)
                val template = CancerchTemplateON256EnUs(resource, TestInfo.ON256)
                page = SectionPage(547f, 65f, resource.fontDefault())

                return CancerchGangbukEnUs(template as CancerchTemplateON256<CancerchResource>, dto, sign, footer, page)
            }

            else -> throw Exception("ERROR : Unknown Service Code : " + service)
        }
    }

    private fun builderGenome(service: String, dto: CancerchDto): CancerchPageBuilder<*>? {
        val doc = PDDocument()
        val page: Painter<CancerchTemplate<CancerchResource>, CancerchDto>

        when (service) {
            TestInfo.N203.code(), TestInfo.N204.code(), TestInfo.N205.code(), TestInfo.N206.code(), TestInfo.J024.code() -> {
                val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionSign(65f)
                val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionFooterGenomeNotColorBar()
                val resource = CancerchResourceN203KoKr(doc)
                val template = CancerchTemplateN203KoKr(resource, TestInfo.N203)
                page = SectionPage(547f, 65f, resource.fontDefault())

                return CancerchN203(template as CancerchTemplateN203<CancerchResource>, dto, sign, footer, page)
            }

            TestInfo.ON203.code() -> {
                val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto> =
                    com.greencross.lims.report.enus.SectionSign(65f)
                val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> =
                    SectionFooterEngGenomeNotColorBar()
                val resource = CancerchResourceON203EnUs(doc)
                val template = CancerchTemplateON203EnUs(resource, TestInfo.ON203)
                page = SectionPage(547f, 65f, resource.fontDefault())

                return CancerchON203(template as CancerchTemplateON203<CancerchResource>, dto, sign, footer, page)
            }

            else -> throw Exception("ERROR : Unknown Service Code : " + service)
        }
    }

    private fun builderDNACX(dto: DNACXDto): DNACXPageBuilder<*> {
        val doc = PDDocument()
        val page: Painter<DNACXTemplate<DNACXResource>, DNACXDto>
        val sign: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = com.greencross.lims.report.enus.SectionSign(65f)
        val footer: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionFooterWithLymphotec()
        println(dto.language)
        return when (dto.language) {
            "en-us" -> {
                val template = DNACXTemplateON204EnUs(DNACXResourceON204EnUs(doc), TestInfo.ON204)
                page = SectionPage(547f, 65f, template.resource().fontDefault())
                DNACXON204EnUs(template as DNACXTemplateON204<DNACXResource>, dto, sign, footer, page)
            }

            "ja-jp" -> {
                val template = DNACXTemplateON204JaJp(DNACXResourceON204JaJp(doc), TestInfo.ON204)
                page = SectionPage(547f, 65f, template.resource().fontDefault())
                DNACXON204JaJp(template as DNACXTemplateON204<DNACXResource>, dto, sign, footer, page)
            }

            else -> throw Exception("잘못된 언어 구분입니다.")
        }
    }

    private fun builderDNACT(dto: DNACTDto): DNACTPageBuilder<*> {
        val doc = PDDocument()
        val page: Painter<DNACTTemplate<DNACTResource>, DNACTDto>
        val sign: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = com.greencross.lims.report.enus.SectionSign(65f)
        val footer: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionFooterWithLymphotec()
        return when (dto.language) {
            "en-us" -> {
                val template = DNACTTemplateON206EnUs(DNACTResourceON206EnUs(doc), TestInfo.ON206)
                page = SectionPage(547f, 65f, template.resource().fontDefault())
                DNACTON206EnUs(template as DNACTTemplateON206<DNACTResource>, dto, sign, footer, page)
            }

            "ja-jp" -> {
                val template = DNACTTemplateON206JaJp(DNACTResourceON206JaJp(doc), TestInfo.ON206)
                page = SectionPage(547f, 65f, template.resource().fontDefault())
                DNACTON206JaJp(template as DNACTTemplateON206<DNACTResource>, dto, sign, footer, page)
            }

            else -> throw Exception("잘못된 언어 구분입니다.")
        }
    }

    private fun builderLabsGenome(service: String, dto: CancerchDto): CancerchPageBuilder<*>? {
        val doc = PDDocument()
        val page: Painter<CancerchTemplate<CancerchResource>, CancerchDto>

        when (service) {
            TestInfo.N203.code(), TestInfo.N204.code(), TestInfo.N205.code(), TestInfo.N206.code(), TestInfo.J024.code() -> {
                val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionSign(65f)
                val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> =
                    SectionFooterGenomeLabsNotColorBar()
                val resource = CancerchResourceN203KoKr(doc)
                val template = CancerchTemplateN203KoKr(resource, TestInfo.N203)
                page = SectionPage(547f, 65f, resource.fontDefault())

                return CancerchN203(template as CancerchTemplateN203<CancerchResource>, dto, sign, footer, page)
            }

            TestInfo.ON203.code() -> {
                val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto> =
                    com.greencross.lims.report.enus.SectionSign(65f)
                val resource = CancerchResourceON203EnUs(doc)
                val template = CancerchTemplateON203EnUs(resource, TestInfo.ON203)
                val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> =
                    SectionFooterEngGenomeLabsNotColorBar()
                page = SectionPage(547f, 65f, resource.fontDefault())
                return CancerchON203(template as CancerchTemplateON203<CancerchResource>, dto, sign, footer, page)
            }

            else -> throw Exception("ERROR : Unknown Service Code : " + service)
        }
    }

    private fun stringToEnum(result: String) = when (result) {
        "GENERAL" -> CancerRepo.결과.GENERAL
        "CONCERN" -> CancerRepo.결과.CONCERN
        "RISK" -> CancerRepo.결과.RISK
        else -> throw Exception("분류되지 않은 결과지 출력 위험군입니다.")
    }

    private fun stringToCTEnum(result: String) = when (result) {
        "NOT_DETECTED" -> DNACTDto.Risk.NOT_DETECTED
        "WEAK" -> DNACTDto.Risk.WEAK
        "MODE" -> DNACTDto.Risk.MODERATE
        "STRONG" -> DNACTDto.Risk.STRONG
        else -> throw Exception("분류되지 않은 결과지 출력 위험군입니다.")
    }

    private fun stringToCXEnum(result: String) = when (result) {
        "LOW" -> DNACXDto.Risk.LOW
        "MILD" -> DNACXDto.Risk.MILD
        "MODERATE" -> DNACXDto.Risk.MODERATE
        "HIGH" -> DNACXDto.Risk.HIGH
        else -> throw Exception("분류되지 않은 결과지 출력 위험군입니다.")
    }

    private fun stringToCancer(result: String) = when (result) {
        "LuC" -> CancerRepo.암종.폐암
        "Panc" -> CancerRepo.암종.췌장담도암
        "HCC" -> CancerRepo.암종.간암
        "colon" -> CancerRepo.암종.대장암
        "Others" -> CancerRepo.암종.기타암종
        "ESO" -> CancerRepo.암종.식도암
        "OV" -> CancerRepo.암종.난소암
        else -> throw Exception("구분되지 않은 결과지 출력 암종입니다.")
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

    private fun cancerToName(cancer: String) = when (cancer) {
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
