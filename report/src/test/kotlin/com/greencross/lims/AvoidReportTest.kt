package com.greencross.lims

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
import java.awt.Desktop
import java.io.File
import java.time.LocalDate
import java.time.Period
import java.time.temporal.TemporalAdjusters
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class AvoidReportTest {
    fun test() {
        val doc: PDDocument? = build(null, "ko-kr");
        if (doc != null) {
            doc.save("avoid.pdf")
            Desktop.getDesktop().open(File("avoid.pdf"))
        }
    }

    fun build(obj: JvmType.Object?, lang: String): PDDocument? {
        val type: LogoType = LogoType.DEPENDENT
        val repo: CancerRepo = CancerRepo()
        return builder(TestInfo.N201, type,
            AvoidDto("TT-5-412",
                AvoidDto.Results.고위험,
                AvoidDto.Cancer("유방암", repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.유방암, 57, Sex.F)!!, repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.유방암, 57, Sex.F)!!, 95.5),
                AvoidDto.Cancer("간암", repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.간암, 57, Sex.F)!!, repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.간암, 57, Sex.F)!!, 13.8)))?.build()
//            AvoidDto("TT-5-412",
//                AvoidDto.Results.기타암종,
//                AvoidDto.Cancer("기타암종", repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.모든암, 57, Sex.F)!!, repo.findASRbyAgeAndCancerAndSex(CancerRepo.암종.모든암, 57, Sex.F)!!, 95.5),
//                AvoidDto.Cancer("", 0.0, 0.0)))?.build()
//        AvoidDto("TT-5-412",
//            AvoidDto.Results.저위험,
//            AvoidDto.Cancer("", 0.0,0.0, 95.5),
//            AvoidDto.Cancer("", 0.0, 0.0, 13.8)))?.build()
    }
    private fun builder(test: TestInfo, logo: LogoType, dto: AvoidDto) : AvoidPageBuilder<*>? {
        val doc = PDDocument()
        dto.barcode = test.code()
        dto.medicalInstitution = "아이메드 강남의원"
        dto.requestNumber = "20210702-171-5002"
        dto.patientName = "홍길동"
        dto.birthDate = LocalDate.of(1966,4,29)
        dto.sex = Sex.F
        dto.medicalRecordNumber = "-"
        dto.specimenType = "Whole Blood"
        dto.collectionDate = LocalDate.of(2021,7,2)
        dto.receiptDate = LocalDate.of(2021,7,2)
        dto.reportDate = LocalDate.of(2021,7,2)
        dto.age = age(dto.birthDate, dto.collectionDate)

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
    private fun age(birth: LocalDate?, sampling: LocalDate?): String {
        if (birth == null) return "-"
        return if (sampling == null) (Period.between(
            birth,
            LocalDate.now().with(TemporalAdjusters.firstDayOfYear())
        ).years + 1).toString() else (Period.between(
            birth,
            sampling.with(TemporalAdjusters.firstDayOfYear())
        ).years + 1).toString()
    }
}

fun main(){
    val test = AvoidReportTest()
    test.test()
}