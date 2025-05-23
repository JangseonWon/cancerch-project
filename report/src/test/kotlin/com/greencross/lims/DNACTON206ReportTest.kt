package com.greencross.lims

import com.gcgenome.lims.avoid.TestInfo
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.SectionFooterWithLymphotec
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
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.kokr.SectionPage
import com.greencross.lims.report.enus.SectionSign
import org.apache.pdfbox.pdmodel.PDDocument
import java.awt.Desktop
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime

class DNACTON206ReportTest {
    var code = "375"
    var patient: String = "閔東熙" //24자 제한
    var birth: Int = 1994
    var collection: LocalDate = LocalDate.now().minusDays(7)
    var language = "ja-jp"
    var sex: Sex = Sex.F
    var receipt: LocalDate = collection
    var barcode: String = "CR3-$code"
    var request: String = "20211109-971-0$code"
    val cancer = "폐암"
    val comment: String = "ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ"
    val risk = DNACTDto.Risk.NOT_DETECTED
    var dto = DNACTDto(barcode, risk,
            listOf(
                DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 1.9f, 5.4f, 1.019f, 0.548f),
                DNACTDto.SummaryOfResult(LocalDate.of(2025,3, 24), "Thyroid cancer", 3.9f, 3.4f, 0.019f, 0.548f),
                DNACTDto.SummaryOfResult(LocalDate.of(2025,2, 24), "Thyroid cancer", 3.9f, 3.4f, 0.019f, 0.548f),
                DNACTDto.SummaryOfResult(LocalDate.of(2025,1,  7),"Thyroid cancer",3.3f,7.34f,0.006f,0.023f),
                DNACTDto.SummaryOfResult(LocalDate.of(2024,9, 24), "Thyroid cancer", 3.9f, 3.4f, 0.019f, 0.548f)
            ), language).apply { this.comment = comment}
    fun test() {
        val doc: PDDocument? = build()
        if (doc != null) {
            doc.save("./N203/샘플테테테스트.pdf")
            Desktop.getDesktop().open(File("./N203/샘플테테테스트.pdf"))
        }
    }
    val sors = listOf(
        Triple("양성0개_", DNACTDto.Risk.NOT_DETECTED ,            listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 1.9f, 3.4f, 0.422f, 0.528f))), // 양성 0개
        Triple("양성1개(cf)_", DNACTDto.Risk.WEAK ,                listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 10.9f, 3.4f, 0.422f, 0.528f))),// 양성 1개
        Triple("양성1개(is)_", DNACTDto.Risk.MODERATE,             listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 1.9f, 5.4f, 0.422f, 0.528f))), // 양성 1개
        Triple("양성1개(cov)_", DNACTDto.Risk.WEAK,                listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 1.9f, 3.4f, 0.522f, 0.528f))), // 양성 1개
        Triple("양성1개(fems)_", DNACTDto.Risk.WEAK,               listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 1.9f, 3.4f, 0.422f, 0.628f))), // 양성 1개
        Triple("양성2개(cf, is)_", DNACTDto.Risk.MODERATE,         listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 10.9f, 5.4f, 0.422f, 0.528f))), // 양성 2개
        Triple("양성2개(cf, cov)_", DNACTDto.Risk.WEAK,            listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 10.9f, 3.4f, 0.522f, 0.528f))), // 양성 2개
        Triple("양성2개(cf, fems)_", DNACTDto.Risk.WEAK,           listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 10.9f, 3.4f, 0.422f, 0.628f))), // 양성 2개
        Triple("양성2개(is, cov)_", DNACTDto.Risk.MODERATE,        listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 1.9f, 5.4f, 0.522f, 0.528f))), // 양성 2개
        Triple("양성2개(is, fems)_", DNACTDto.Risk.MODERATE,       listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 1.9f, 5.4f, 0.422f, 0.628f))), // 양성 2개
        Triple("양성2개(cov,fems)_", DNACTDto.Risk.WEAK,           listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 1.9f, 3.4f, 0.522f, 0.628f))), // 양성 2개
        Triple("양성3개(cf,is,cov)_", DNACTDto.Risk.MODERATE,      listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 10.9f, 5.4f, 0.522f, 0.528f))), // 양성 3개
        Triple("양성3개(cf,is,fems)_", DNACTDto.Risk.MODERATE,     listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 10.9f, 5.4f, 0.422f, 0.628f))), // 양성 3개
        Triple("양성3개(cf,cov,fems)_", DNACTDto.Risk.MODERATE,    listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 10.9f, 3.4f, 0.522f, 0.628f))), // 양성 3개
        Triple("양성3개(is,cov,fems)_", DNACTDto.Risk.MODERATE,    listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 1.9f, 5.4f, 0.522f, 0.628f))), // 양성 3개
        Triple("양성4개_", DNACTDto.Risk.STRONG,       listOf(DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 10.9f, 5.4f, 0.522f, 0.628f))), // 양성 4개
    )
    fun createAllReport() {
        for (sor in sors) {
            dto.apply {
                result = sor.third
                risk = sor.second
            }
            dto.patientName = this.patient
            dto.birthDate = LocalDate.of(this.birth, 1, 1)
            dto.sex = this.sex
            dto.requestNumber = this.request
            dto.collectionDate = this.collection
            dto.receiptDate = this.receipt
            dto.medicalInstitution = "GC Genome"
            dto.medicalRecordNumber = barcode
            dto.specimenType = "Whole Blood"
            dto.reportDate = LocalDate.now()
            val age = age(dto.birthDate, dto.collectionDate)
            dto.age = age.toString()
            if(sor.second != DNACTDto.Risk.NOT_DETECTED) dto.result[0].apply {
                this.FEMSPath = "D:\\avoid-project\\report\\src\\test\\resources\\양성예시_fems.png"
                this.GenomicPath = "D:\\avoid-project\\report\\src\\test\\resources\\양성예시_iscore.png"
            }
            println("./ON206/${sor.first}${dto.risk}.pdf")
            builder(dto).build().save("./ON206/${sor.first}${dto.risk}_${language}.pdf")
        }
    }

    fun build(): PDDocument {
//        val dto = DNACTDto(barcode, risk,
//            listOf(
//                DNACTDto.SummaryOfResult(LocalDate.now(), "Thyroid cancer", 1.9f, 5.4f, 1.019f, 0.548f),
//                DNACTDto.SummaryOfResult(LocalDate.of(2025,3, 24), "Thyroid cancer", 3.9f, 3.4f, 0.019f, 0.548f),
//                DNACTDto.SummaryOfResult(LocalDate.of(2025,2, 24), "Thyroid cancer", 3.9f, 3.4f, 0.019f, 0.548f),
//                DNACTDto.SummaryOfResult(LocalDate.of(2025,1,  7),"Thyroid cancer",3.3f,7.34f,0.006f,0.023f),
//                DNACTDto.SummaryOfResult(LocalDate.of(2024,9, 24), "Thyroid cancer", 3.9f, 3.4f, 0.019f, 0.548f)
//            ), language)
        dto.patientName = this.patient
        dto.birthDate = LocalDate.of(this.birth, 1, 1)
        dto.sex = this.sex
        dto.requestNumber = this.request
        dto.collectionDate = this.collection
        dto.receiptDate = this.receipt
        dto.medicalInstitution = "GC Genome"
        dto.medicalRecordNumber = barcode
        dto.specimenType = "Whole Blood"
        dto.reportDate = LocalDate.of(2022,11,3)
        val age = age(dto.birthDate, dto.collectionDate)
        dto.age = age.toString()

        return builder(dto).build()
    }

    private fun builder(dto: DNACTDto): DNACTPageBuilder<*> {
        val doc = PDDocument()
        val page: Painter<DNACTTemplate<DNACTResource>, DNACTDto>
        val sign: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionSign(65f)
        val footer: Painter<DNACTTemplate<DNACTResource>, DNACTDto> = SectionFooterWithLymphotec()
        return when(dto.language) {
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
fun main() {
    val test = DNACTON206ReportTest()
//    test.test()
    test.createAllReport()
}
