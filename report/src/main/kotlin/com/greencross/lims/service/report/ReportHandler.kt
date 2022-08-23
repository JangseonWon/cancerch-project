package com.greencross.lims.service.report

import com.fasterxml.jackson.databind.ObjectMapper
import com.gcgenome.lims.avoid.TestInfo
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
import com.greencross.lims.report.kokr.SectionFooterGenomeLabs
import com.greencross.lims.report.kokr.SectionPage
import com.greencross.lims.report.kokr.SectionSign
import com.greencross.lims.service.analysis.AnalysisDao
import com.greencross.lims.service.reportfile.ReportFileRepository
import org.apache.pdfbox.pdmodel.PDDocument
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*

@Service
class ReportHandler(
    private val analysisDao: AnalysisDao,
    private val reportDao : ReportDao,
    private val cancerRepo: CancerRepo,
    private val fileRepo: ReportFileRepository,
    private val mapper: ReportMapper,
    private val om : ObjectMapper
) {
    @Transactional
    fun reports(sample: Long, service: String): Flux<Report> {
        return reportDao.findBySampleAndService(sample, service)
    }

    @Transactional
    fun print(sample: Long, service: String, lang: String) : Mono<com.greencross.lims.data.Report>{
        return analysisDao.findById(sample, service).flatMap{
            val dto = analysisToAvoidDto(it)
            val baos = ByteArrayOutputStream()
            val doc = build(lang, dto)
            val createTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(LocalDateTime.now().toInstant(OffsetDateTime.now().offset).toEpochMilli()), ZoneId.systemDefault()
            )
            doc?.save(baos)
            val fileName = "${it.sample}_${it.service}_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"))}.pdf"
            val reportFile = ReportFile(UUID.randomUUID()).apply {
                this.createTime = createTime
                this.data = ByteBuffer.wrap(baos.toByteArray())
                this.extension = "pdf"
                this.name = fileName
                this.size = baos.toByteArray().size.toLong()
            }
            fileRepo.save(reportFile)
            val entity = com.greencross.lims.entity.Report(it.sample, it.service, createTime).apply{
                this.file = reportFile.id
                this.name = reportFile.name!!
                this.size = reportFile.size
            }
            reportDao.create(entity).map(mapper::toDto)
        }
    }
    @Transactional
    fun preview(sample: Long, service: String, createAt: Long): Mono<ByteArray>{
        return reportDao.findForCassandraReport(sample, service, LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId()))
            .map {
                fileRepo.findById(it.file)
            }
            .map {it.get().data!!.array()}
    }
    private fun analysisToAvoidDto(analysis: Analysis) : AvoidDto{
        val patient = analysis.patient
        val barcode = analysis.barcode.toString()
        val (customerName, requestNumber) = if(patient.customerCode2!=null) Pair(patient.customerName2!!, formatSampleId(
            analysis.remark?.toLongOrNull()
        ))
        else Pair(patient.customerName, formatSampleId(analysis.sample))
        val result = if(sex(patient.sex) == Sex.M) analysis.too5Pred else analysis.too6Pred
        val cancer1  = when(stringToEnum(analysis.result)) {
            CancerRepo.결과.GENERAL -> AvoidDto.Cancer()
            CancerRepo.결과.CONCERN -> AvoidDto.Cancer("기타암종")
            else                    -> AvoidDto.Cancer(cancerToFileName(result),
                        cancerRepo.findPPVbyAgeAndCancerAndSex(
                            stringToCancer(result), age(patient.birth), sex(patient.sex))!!,
                        cancerRepo.findASRbyAgeAndCancerAndSex(
                            stringToCancer(result), age(patient.birth), sex(patient.sex))!!)
        }

        val avoidDto = AvoidDto(barcode, stringToResult(analysis.result), cancer1)
        avoidDto.barcode = barcode
        avoidDto.patientName = patient.name
        avoidDto.birthDate = patient.birth
        avoidDto.age = age(avoidDto.birthDate).toString()
        avoidDto.sex = sex(patient.sex)
        avoidDto.requestNumber = requestNumber
        avoidDto.collectionDate = analysis.dateSampling.toLocalDate()
        avoidDto.receiptDate = analysis.dateRequest.toLocalDate()
        avoidDto.reportDate = analysis.dateDue.toLocalDate()
        avoidDto.medicalRecordNumber = patient.mrn
        avoidDto.barcode = barcode
        avoidDto.medicalInstitution = customerName
        avoidDto.medicalRecordNumber = patient.code
        avoidDto.specimenType = analysis.sampleType

        return avoidDto
    }

    private fun age(birth: LocalDate?) : Int {
        return Period.between(birth, LocalDate.now().with(TemporalAdjusters.firstDayOfYear())).years+1
    }
    private fun sex(sex: String) : Sex{
        return Sex.valueOf(sex)
    }
    private fun build(lang: String, dto: AvoidDto): PDDocument? {
        return builder(TestInfo.N201, LogoType.DEPENDENT, dto)?.build()
    }

    private fun builder(test: TestInfo, logo: LogoType, dto: AvoidDto) : AvoidPageBuilder<*>? {
        val doc = PDDocument()

        val sign: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionSign(65f)
        val footer: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionFooterGenomeLabs()
        val page: Painter<AvoidTemplate<AvoidResource>, AvoidDto>
        return if(TestInfo.N201 == test){
            var resource = AvoidResourceN201KoKr(doc)
            var template = AvoidTemplateN201KoKr(resource, test)

            page = SectionPage(547f, 65f, resource.fontDefault())

            return AvoidN201(template as AvoidTemplateN201<AvoidResource>, dto, sign, footer, page)
        } else null
    }
    private fun stringToEnum(result: String) = when(result){
        "GENERAL" -> CancerRepo.결과.GENERAL
        "CONCERN" -> CancerRepo.결과.CONCERN
        else      -> CancerRepo.결과.RISK
    }
    private fun stringToCancer(result: String) = when(result){
        "LuC"    -> CancerRepo.암종.폐암
        "Panc"   -> CancerRepo.암종.췌장암
        "HCC"    -> CancerRepo.암종.간암
        "colon"  -> CancerRepo.암종.대장암
        "etc"    -> CancerRepo.암종.기타암종
        "ESO"    -> CancerRepo.암종.식도암
        else     -> CancerRepo.암종.유방암
    }
    private fun cancerToFileName(cancer: String) = when(cancer){
        "LuC"    -> "폐암"
        "Panc"   -> "췌장암"
        "HCC"    -> "간암"
        "colon"  -> "대장암"
        "etc"    -> "기타암종"
        "ESO"    -> "식도암"
        else     -> "유방암"
    }
    private fun stringToResult(result:String) = when(result){
        "GENERAL" -> AvoidDto.Results.GENERAL
        "CONCERN" -> AvoidDto.Results.CONCERN
        else      -> AvoidDto.Results.RISK
    }
    private fun formatSampleId(id: Long?): String? {
        if (id == null) return null
        val cast = id.toString()
        return if (cast.length == 15) "${cast.substring(0, 8)}-${cast.substring(8, 11)}-${cast.substring(11)}"
        else cast
    }
}