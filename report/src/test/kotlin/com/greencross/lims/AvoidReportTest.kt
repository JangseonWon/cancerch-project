package com.greencross.lims

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.avoid.*
import com.greencross.lims.report.avoid.kokr.AvoidResourceN201KoKr
import com.greencross.lims.report.avoid.kokr.AvoidTemplateN201KoKr
import com.greencross.lims.report.avoid.repository.CancerRepo
import com.greencross.lims.report.builder.LogoType
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.func.Painter
import com.greencross.lims.report.kokr.SectionFooterGenome
import com.greencross.lims.report.kokr.SectionFooterGenomeLabs
import com.greencross.lims.report.kokr.SectionPage
import com.greencross.lims.report.kokr.SectionSign
import org.apache.pdfbox.pdmodel.PDDocument
import java.awt.Desktop
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


class AvoidReportTest {
    val birthyears = arrayOf(64, 60, 62, 60, 60, 64, 67, 67, 71, 67, 61, 57, 59, 59, 59, 53, 45, 56, 59)
    val patients = arrayOf("김미희", "한종수", "한은수", "이용탁", "신일용", "허옥상", "신유진", "안미자", "송윤하", "김의수", "김금순", "김양순", "김명순", "김승주", "홍순철", "김옥남", "임경천", "김창수", "심오복")
    val codes = arrayOf(345,346,147,148,152,153,290,291,364,365,366,367,368,369,370,371,372,374,375)
    val sexes2 = arrayOf(Sex.F, Sex.M, Sex.F, Sex.M, Sex.M, Sex.M,Sex.F, Sex.F, Sex.F, Sex.M, Sex.F, Sex.F, Sex.F, Sex.M, Sex.M, Sex.F, Sex.M, Sex.M, Sex.F)
    val collections = arrayOf(
        LocalDate.of(2021,11,11),LocalDate.of(2021,11,11),LocalDate.of(2021,11,10),LocalDate.of(2021,11,10),LocalDate.of(2021,11,11),
        LocalDate.of(2021,11,11),LocalDate.of(2021,11,11),LocalDate.of(2021,11,11),LocalDate.of(2021,11,20),LocalDate.of(2021,11,20),
        LocalDate.of(2021,11,20),LocalDate.of(2021,11,20),LocalDate.of(2021,11,20),LocalDate.of(2021,11,20),LocalDate.of(2021,11,20),
        LocalDate.of(2021,11,20),LocalDate.of(2021,11,20),LocalDate.of(2021,11,20),LocalDate.of(2021,11,20))
    var code = "375"
    var patient: String = "심오복"
    var birth: Int = 1959
    var collection: LocalDate = LocalDate.of(2021,11,20)
//    val sex: Sex = Sex.M
    var sex: Sex = Sex.F
    var receipt: LocalDate = collection
    val cancers = arrayOf(CancerRepo.암종.폐암, CancerRepo.암종.췌장담도암, CancerRepo.암종.대장암, CancerRepo.암종.난소암, CancerRepo.암종.식도암, CancerRepo.암종.간암)
    val sexes = arrayOf(Sex.M, Sex.F)
    val birthes = arrayOf(1940, 1950, 1960, 1970, 1980, 1990, 2000)
    var cancer= CancerRepo.암종.기타암종
    var barcode: String = "CR3-$code"
    var request: String = "20211109-971-0$code"
    val comment: String = "ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ"
    fun test() {
//        for (sex in sexes){
//            this.sex = sex
//            for (birth in birthes) {
//                this.birth = birth
//                val doc: PDDocument? = build(null, "ko-kr");
//                doc!!.save("./기타암종/집중관리_${this.sex}_${this.birth}.pdf")
//            }
//        }
//        for (cancer in cancers) {
//            this.cancer = cancer
//            for (sex in sexes) {
//                this.sex = sex
//                for (birth in birthes) {
//                    this.birth = birth
//                    val doc: PDDocument? = build(null, "ko-kr");
//                    doc!!.save("./일반관리/일반관리_${this.sex}_${this.birth}.pdf")
//                }
//            }
//        }
        for (i in sexes2.indices){
            this.code = codes[i].toString()
            this.birth = birthyears[i]+1900
            this.sex = sexes2[i]
            this.patient = patients[i]
            this.collection = collections[i]
            this.receipt = collection
            this.barcode = "CR3-$code"
            this.request = "20211109-971-0$code"
            val doc: PDDocument? = build(null, "ko-kr")
            doc!!.save("./"+this.barcode+".pdf")
            print(barcode)
        }
//        val doc: PDDocument? = build(null, "ko-kr")
//        if (doc != null) {
//            doc.save("./"+this.barcode+".pdf")
//            Desktop.getDesktop().open(File("./"+this.barcode+".pdf"))
//        }
    }

    fun build(obj: JvmType.Object?, lang: String): PDDocument? {
        val type: LogoType = LogoType.DEPENDENT
        val repo: CancerRepo = CancerRepo()
        return builder(
            TestInfo.N201, type,
//            AvoidDto("TT-5-412",
//                AvoidDto.Results.RISK,
//                AvoidDto.Cancer(cancer.name, repo.findPPVbyAgeAndCancerAndSex(cancer, age(LocalDate.of(this.birth,1,1), collection).toInt(), sex)!!,
//                    repo.findASRbyAgeAndCancerAndSex(cancer, age(LocalDate.of(this.birth,1,1), collection).toInt(), sex)!!, 95.5, comment)))?.build()
//            AvoidDto("TT-5-412",
//                AvoidDto.Results.CONCERN,
//                AvoidDto.Cancer("기타암종")))?.build()
            AvoidDto("",
                AvoidDto.Results.GENERAL,
                AvoidDto.Cancer()))?.build()
    }
    private fun builder(test: TestInfo, logo: LogoType, dto: AvoidDto) : AvoidPageBuilder<*>? {
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

        dto.reportDate = LocalDate.of(2022,11,3)
        dto.age = age( dto.birthDate, dto.collectionDate).toString()

        val sign: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionSign(65f)
        val footer: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionFooterGenome()//SectionFooterGenomeLabs()
        val page: Painter<AvoidTemplate<AvoidResource>, AvoidDto>
        return if(TestInfo.N201 == test){
            var resource = AvoidResourceN201KoKr(doc)
            var template = AvoidTemplateN201KoKr(resource, test)

            page = SectionPage(547f, 65f, resource.fontDefault())

            return AvoidN201(template as AvoidTemplateN201<AvoidResource>, dto, sign, footer, page)
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
    val test = AvoidReportTest()
    test.test()
}