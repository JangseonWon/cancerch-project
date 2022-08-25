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
import com.greencross.lims.report.kokr.SectionPage
import com.greencross.lims.report.kokr.SectionSign
import org.apache.pdfbox.pdmodel.PDDocument
import java.time.LocalDate
import java.time.Period
import java.time.temporal.TemporalAdjusters
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


class AvoidReportTest {
    var code = "451"
    val patient: String = "김승전"
    var birth: Int = 1970
    val collection: LocalDate = LocalDate.of(2022,3,25)
//    val sex: Sex = Sex.M
    var sex: Sex = Sex.F
    val receipt: LocalDate = collection
    val cancers = arrayOf(CancerRepo.암종.폐암, CancerRepo.암종.췌장암, CancerRepo.암종.대장암, CancerRepo.암종.난소암, CancerRepo.암종.식도암, CancerRepo.암종.간암)
    val sexes = arrayOf(Sex.M, Sex.F)
    val birthes = arrayOf(1940, 1950, 1960, 1970, 1980, 1990, 2000)
    var cancer= CancerRepo.암종.간암
    val barcode: String = "CR3-$code"
    val request: String = "2021109-971-0$code"
    fun test() {
//        for (cancer in cancers) {
//            this.cancer = cancer
            for (sex in sexes) {
                this.sex = sex
                for (birth in birthes) {
                    this.birth = birth
                    val doc: PDDocument? = build(null, "ko-kr");
                    doc!!.save("./일반관리/일반관리_${this.sex}_${this.birth}.pdf")
                }
            }
//        }
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
//                    repo.findASRbyAgeAndCancerAndSex(cancer, age(LocalDate.of(this.birth,1,1), collection).toInt(), sex)!!, 95.5)))?.build()
//            AvoidDto("TT-5-412",
//                AvoidDto.Results.CONCERN,
//                AvoidDto.Cancer("기타암종")))?.build()
            AvoidDto(this.barcode,
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

        dto.barcode = dto.barcode
        dto.medicalInstitution = "GC지놈"
        dto.medicalRecordNumber = "-"
        dto.specimenType = "Whole Blood"

        dto.reportDate = LocalDate.of(2022,5,26)
        dto.age = age(dto.birthDate, dto.collectionDate)

        val sign: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionSign(65f)
        val footer: Painter<AvoidTemplate<AvoidResource>, AvoidDto> = SectionFooterGenome()
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