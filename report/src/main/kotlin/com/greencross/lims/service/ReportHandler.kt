package com.greencross.lims.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
import com.greencross.lims.test.avoid.TestInfo
import org.apache.pdfbox.pdmodel.PDDocument
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.OutputStream
import java.time.LocalDate
import java.time.Period
import java.time.temporal.TemporalAdjusters

class ReportHandler(
    private val analysisDao: AnalysisDao,
    private val reportDao : ReportDao,
    private val cancerRepo: CancerRepo,
    private val om : ObjectMapper
) {
    @Transactional
    fun reports(sample: Long, service: String): Flux<Report> {
        return reportDao.findBySampleAndService(sample, service)
    }

    @Transactional
    fun print(sample: Long, service: String) : Mono<Report>{
        return analysisDao.findById(sample, service).map{
            analysisToAvoidDto(it)
        }
    }
    fun analysisToAvoidDto(analysis: Analysis) : AvoidDto{
        val patient = analysis.patient
        val valueMap : Map<String, String> =  om.readValue<Map<String, String>>(analysis.value!!)
        val barcode = valueMap["barcode"]
        val result = stringToEnum(valueMap["result"]!!)
        val cancer1 = AvoidDto.Cancer(valueMap["first"]!!,
            cancerRepo.findPPVbyAgeAndCancerAndSex(result,CancerRepo.암종.valueOf(valueMap["first"]!!), age(patient.birth!!.toLocalDate()), sex(patient.sex))!!,
            cancerRepo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.valueOf(valueMap["first"]!!), age(patient.birth.toLocalDate()), sex(patient.sex))!!,
            valueMap["firstScore"]!!.toDouble())
        val cancer2 = AvoidDto.Cancer(valueMap["second"]!!,
            cancerRepo.findPPVbyAgeAndCancerAndSex(result, CancerRepo.암종.valueOf(valueMap["second"]!!), age(patient.birth.toLocalDate()), sex(patient.sex))!!,
            cancerRepo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.valueOf(valueMap["second"]!!), age(patient.birth.toLocalDate()), sex(patient.sex))!!,
            valueMap["secondScore"]!!.toDouble()
            )
        val avoidDto = AvoidDto(barcode, AvoidDto.Results.valueOf(valueMap["result"]!!), cancer1, cancer2)
        avoidDto.barcode = barcode
        avoidDto.patientName = patient.name
        avoidDto.birthDate = patient.birth.toLocalDate()
        avoidDto.age = age(patient.birth.toLocalDate()).toString()
        avoidDto.sex = sex(patient.sex)
        avoidDto.requestNumber = analysis.sample.toString()
        avoidDto.medicalRecordNumber = patient.mrn
        avoidDto.barcode = barcode
        avoidDto.medicalInstitution = patient.customerName
        avoidDto.medicalRecordNumber = patient.code
        avoidDto.specimenType = analysis.sampleType
        val doc: PDDocument? = builder(TestInfo.N201, LogoType.DEPENDENT, avoidDto)?.build()
        val os : OutputStream? = null
        builder(TestInfo.N201, LogoType.DEPENDENT, avoidDto)?.build()!!.save(os)

    }

    private fun age(birth: LocalDate?) : Int {
        return Period.between(birth, LocalDate.now().with(TemporalAdjusters.firstDayOfYear())).years+1
    }
    private fun sex(sex: String) : Sex{
        return Sex.valueOf(sex)
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
        "일반관리" -> CancerRepo.결과.NORMAL
        "관심관리" -> CancerRepo.결과.ATTENTION
        else -> CancerRepo.결과.CONCENT
    }
}