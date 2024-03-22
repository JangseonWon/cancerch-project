package com.greencross.lims

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.avoid.repository.CancerRepo
import com.greencross.lims.report.builder.LogoType
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.cancerch.*
import com.greencross.lims.report.cancerch.enus.CancerchResourceON203EnUs
import com.greencross.lims.report.cancerch.enus.CancerchTemplateON203EnUs
import com.greencross.lims.report.cancerch.kokr.CancerchResourceN203KoKr
import com.greencross.lims.report.cancerch.kokr.CancerchTemplateN203KoKr
import com.greencross.lims.report.func.Painter
import com.greencross.lims.report.kokr.SectionFooterGenomeNotColorBar
import com.greencross.lims.report.kokr.SectionPage
import com.greencross.lims.report.kokr.SectionSign
import org.apache.pdfbox.pdmodel.PDDocument
import java.awt.Desktop
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


class CancerchReportTest {
    var code = "375"
    var patient: String = "홍길동"
    var birth: Int = 1968
    var collection: LocalDate = LocalDate.of(2023,10,11)
    var sex: Sex = Sex.F
    var receipt: LocalDate = collection
    var cancer= CancerRepo.암종.기타암종
    var barcode: String = "CR3-$code"
    var request: String = "20231011-971-0001"
    val comment: String = "소견이 입력됩니다."
    fun test() {
        val doc: PDDocument? = build(null, "ko-kr")
        if (doc != null) {
            doc.save("./N203/샘플테테테스트.pdf")
            Desktop.getDesktop().open(File("./N203/샘플테테테스트.pdf"))
        }
    }

    fun build(obj: JvmType.Object?, lang: String): PDDocument? {
        val type: LogoType = LogoType.DEPENDENT
        val repo: CancerRepo = CancerRepo()
        return builder(
            TestInfo.N203, type,
            CancerchDto("",
                CancerchDto.Results.RISK,
                CancerchDto.Cancer(cancer.name, repo.findPPVbyAgeAndCancerAndSex(cancer, age(LocalDate.of(this.birth,1,1), collection).toInt(), sex)!!,
                    repo.findASRbyAgeAndCancerAndSex(cancer, age(LocalDate.of(this.birth,1,1), collection).toInt(), sex)!!, 95.5, comment)))?.build()
//            CancerchDto("",
//                CancerchDto.Results.CONCERN,
//                CancerchDto.Cancer("기타암종")))?.build()
//            CancerchDto("",
//                CancerchDto.Results.GENERAL,
//                CancerchDto.Cancer()))?.build()
    }
    private fun builder(test: TestInfo, logo: LogoType, dto: CancerchDto) : CancerchPageBuilder<*>? {
        val doc = PDDocument()

        //변경점
        dto.patientName = this.patient
        dto.birthDate = LocalDate.of(this.birth,1,1)
        dto.sex = this.sex
        dto.requestNumber = this.request
        dto.collectionDate = this.collection
        dto.receiptDate = this.receipt

//        dto.barcode = dto.barcode
        dto.medicalInstitution = "GC지놈"
        dto.medicalRecordNumber = barcode
        dto.specimenType = "Whole Blood"

        dto.reportDate = LocalDate.of(2023,10,24)
        dto.age = age(dto.birthDate, dto.collectionDate).toString()
        val page: Painter<CancerchTemplate<CancerchResource>, CancerchDto>
        val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionSign(65f)
        val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionFooterGenomeNotColorBar()
        return if(TestInfo.N203 == test){

            val resource = CancerchResourceN203KoKr(doc)
            val template = CancerchTemplateN203KoKr(resource, test)

            page = SectionPage(547f, 65f, resource.fontDefault())

            return CancerchN203(template as CancerchTemplateN203<CancerchResource>, dto, sign, footer, page)
        } else null
    }
    private fun age(birth: LocalDate?, sampling: LocalDate?): Int {
        if (birth == null) return 0
        return if (sampling == null) {
            val americanAge = LocalDateTime.now().minusYears(birth.year.toLong()).year.toLong()
            if(birth.plusYears(americanAge).isAfter(LocalDate.now())) americanAge.toInt()-1
            else americanAge.toInt()
        } else {
            val americanAge = sampling.minusYears(birth.year.toLong()).year.toLong()
            if(birth.plusYears(americanAge).isAfter(sampling)) americanAge.toInt()-1
            else americanAge.toInt()
        }
    }

}

fun main(){
    val test = CancerchReportTest()
    test.test()
}
