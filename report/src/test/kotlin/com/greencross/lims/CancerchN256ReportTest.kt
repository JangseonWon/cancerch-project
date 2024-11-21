package com.greencross.lims

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.cancerch.repository.CancerchRepo
import com.greencross.lims.report.builder.LogoType
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.cancerch.*
import com.greencross.lims.report.cancerch.kokr.CancerchResourceN256KoKr
import com.greencross.lims.report.cancerch.kokr.CancerchTemplateN256KoKr
import com.greencross.lims.report.enus.SectionFooterEngGenomeNotColorBar
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.kokr.SectionPage
import com.greencross.lims.report.kokr.SectionSign
import org.apache.pdfbox.pdmodel.PDDocument
import java.awt.Desktop
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


class CancerchN256ReportTest {
    var code = "375"
    var patient: String = "홍길동"
    var birth: Int = 2001
    var collection: LocalDate = LocalDate.of(2024,4,22)
    var sex: Sex = Sex.F
    var receipt: LocalDate = collection
    var cancer= CancerchRepo.암종.기타암종
    var barcode: String = "CR3-$code"
    var request: String = "20231011-971-0001"
    val comment: String = "ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ"

    fun test() {
        val doc: PDDocument? = build(null, "ko-kr")
        if (doc != null) {
            doc.save("./N203/샘플테테테스트.pdf")
            Desktop.getDesktop().open(File("./N203/샘플테테테스트.pdf"))
        }
    }

    fun createAllReport() {
        val cancers = arrayOf(CancerchRepo.암종.폐암, CancerchRepo.암종.췌장담도암, CancerchRepo.암종.대장암, CancerchRepo.암종.난소암, CancerchRepo.암종.식도암, CancerchRepo.암종.간암, CancerchRepo.암종.기타암종)
        val sexes = arrayOf(Sex.M, Sex.F)
        val birthes = arrayOf(1940, 1950, 1960, 1970, 1980, 1990, 2000)

//        for(cancer in cancers) {
//            this.cancer = cancer
//            for (sex in sexes) {
//                this.sex = sex
//                for (birth in birthes) {
//                    this.birth = birth
//                    println("./강북삼성/N256/관심관리/관심관리(${this.cancer})_${this.sex}_${this.birth}.pdf")
//                    val doc: PDDocument? = build(null, "ko-kr")
//                    doc!!.save("./강북삼성/N256/관심관리/관심관리(${this.cancer})_${this.sex}_${this.birth}.pdf")
//                }
//            }
//        }
        for (sex in sexes) {
            this.sex = sex
            for (birth in birthes) {
                this.birth = birth
                println("./강북삼성/N256/일반관리/일반관리_${this.sex}_${this.birth}.pdf")
                val doc: PDDocument? = build(null, "ko-kr")
                doc!!.save("./강북삼성/N256/일반관리/일반관리_${this.sex}_${this.birth}.pdf")
            }
        }
    }

    fun build(obj: JvmType.Object?, lang: String): PDDocument? {
        val type: LogoType = LogoType.DEPENDENT
        val repo: CancerchRepo = CancerchRepo()
        return builder(
            TestInfo.N256, type,
//            CancerchDto("",
//                CancerchDto.Results.RISK,
//                CancerchDto.Cancer(cancer.name, repo.findPPVbyAgeAndCancerAndSex(cancer, age(LocalDate.of(this.birth,1,1), collection).toInt(), sex)!!,
//                    repo.findASRbyAgeAndCancerAndSex(cancer, age(LocalDate.of(this.birth,1,1), collection).toInt(), sex)!!, 95.5, comment)))?.build()
            CancerchDto("",
                CancerchDto.Results.CONCERN,
                CancerchDto.Cancer("기타암종")))?.build()
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
        dto.medicalInstitution = "GC Genome"
        dto.medicalRecordNumber = barcode
        dto.specimenType = "Whole Blood"

        dto.reportDate = LocalDate.of(2022,11,3)
        dto.age = age(dto.birthDate, dto.collectionDate).toString()
        val page: Painter<CancerchTemplate<CancerchResource>, CancerchDto>
        val sign: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionSign(65f)

        return if(TestInfo.N256 == test) {
            val footer: Painter<CancerchTemplate<CancerchResource>, CancerchDto> = SectionFooterEngGenomeNotColorBar()
            val resource = CancerchResourceN256KoKr(doc)
            val template = CancerchTemplateN256KoKr(resource, test)

            page = SectionPage(547f, 65f, resource.fontDefault())

            return CancerchGangbukKoKr(template as CancerchTemplateN256<CancerchResource>, dto, sign, footer, page)
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
    val test = CancerchN256ReportTest()
    test.test()
//    test.createAllReport()
}
