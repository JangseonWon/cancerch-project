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
                AvoidDto.Cancer("유방암", repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.유방암, 57, Sex.F)!!, 95.5),
                AvoidDto.Cancer("간암", repo.findPPVbyAgeAndCancerAndSex(CancerRepo.암종.간암, 57, Sex.F)!!, 13.8)))?.build()
//            AvoidDto("TT-5-412",
//                AvoidDto.Results.기타암종,
//                AvoidDto.Cancer("기타암종", 87.87, 95.3),
//                AvoidDto.Cancer("", 0.0, 0.0)))?.build()
//        AvoidDto("TT-5-412",
//            AvoidDto.Results.저위험,
//            AvoidDto.Cancer("기타암종", 87.87, 95.3),
//            AvoidDto.Cancer("", 0.0, 0.0)))?.build()
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
}

fun main(){
    val test = AvoidReportTest()
    test.test()
}