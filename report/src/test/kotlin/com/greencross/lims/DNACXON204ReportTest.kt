package com.greencross.lims

import com.gcgenome.lims.avoid.TestInfo
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.*
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.enus.resource.DNACXResourceON204EnUs
import com.greencross.lims.report.ON204.template.DNACXTemplate
import com.greencross.lims.report.ON204.enus.template.DNACXTemplateON204EnUs
import com.greencross.lims.report.ON204.jajp.resource.DNACXResourceON204JaJp
import com.greencross.lims.report.ON204.jajp.template.DNACXTemplateON204JaJp
import com.greencross.lims.report.ON204.template.DNACXTemplateON204
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.kokr.SectionPage
import com.greencross.lims.report.enus.SectionSign
import org.apache.pdfbox.pdmodel.PDDocument
import java.awt.Desktop
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime

class DNACXON204ReportTest {
    var code = "375"
    var patient: String = "閔東熙" //24자 제한
    var birth: Int = 1994
    var collection: LocalDate = LocalDate.now().minusDays(7)
    var language = "ja-jp"
    var sex: Sex = Sex.F
    var receipt: LocalDate = collection
    var barcode: String = "CR3-$code"
    var request: String = "20211109-971-0$code"
    val cancer = "췌장담도암"
    val comment: String = "ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ ㅁㅁㅁㅁㅁ"
    val risk = DNACXDto.Risk.HIGH
    var dto = DNACXDto(barcode, risk, DNACXDto.SummaryOfResult(cancer,
        0.8,
        0.1,
        0.6,
        10.5,
        3.32), language)

    fun test() {
        val doc: PDDocument? = build()
        if (doc != null) {
            doc.save("./N203/샘플테테테스트.pdf")
            Desktop.getDesktop().open(File("./N203/샘플테테테스트.pdf"))
        }
    }
    /*ssScoreCutOff             0.779
      ssScoreCutOff99           0.614
      covScoreCutOff            0.423
      femsScoreCutOff           0.531
      cfDNAConcentrationCutOff  9.625
      genomicInstabilityCutOff  4.0*/
    fun createAllReport() {
        val notInscreasedSS = 0.1
        val IncreasedSS95 = 0.4
        val IncreasedSS99 = 0.7
        val NotIncreasedCov = 0.4
        val IncreasedCov = 0.5
        val NotIncreasedFems = 0.4
        val IncreasedFems = 0.8
        val NotIncreasedcfDNA = 8.1
        val IncreasedcfDNA = 10.3
        val NotIncreasedIScore = 3.0
        val IncreasedIScore = 6.0

        val sors = listOf(
            Pair("양성0개_",  DNACXDto(barcode, DNACXDto.Risk.LOW,     DNACXDto.SummaryOfResult(cancer, notInscreasedSS, NotIncreasedCov, NotIncreasedFems, NotIncreasedcfDNA, NotIncreasedIScore), language)),
            Pair("양성1개(ss95)_",  DNACXDto(barcode, DNACXDto.Risk.MODERATE,DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   NotIncreasedCov, NotIncreasedFems, NotIncreasedcfDNA, NotIncreasedIScore), language)),
            Pair("양성1개(ss99)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   NotIncreasedCov, NotIncreasedFems, NotIncreasedcfDNA, NotIncreasedIScore), language)),
            Pair("양성1개(is)_",  DNACXDto(barcode, DNACXDto.Risk.MILD,    DNACXDto.SummaryOfResult(cancer, notInscreasedSS, IncreasedCov,    NotIncreasedFems, NotIncreasedcfDNA, NotIncreasedIScore), language)),
            Pair("양성1개(fems)_",  DNACXDto(barcode, DNACXDto.Risk.MILD,    DNACXDto.SummaryOfResult(cancer, notInscreasedSS, NotIncreasedCov, IncreasedFems,    NotIncreasedcfDNA, NotIncreasedIScore), language)),
            Pair("양성1개(cfDNA)_",  DNACXDto(barcode, DNACXDto.Risk.MILD,    DNACXDto.SummaryOfResult(cancer, notInscreasedSS, NotIncreasedCov, NotIncreasedFems, IncreasedcfDNA,    NotIncreasedIScore), language)),
            Pair("양성1개(iscore)_",  DNACXDto(barcode, DNACXDto.Risk.MILD,    DNACXDto.SummaryOfResult(cancer, notInscreasedSS, NotIncreasedCov, NotIncreasedFems, NotIncreasedcfDNA, IncreasedIScore),    language)),
            Pair("양성2개(ss95,cov)_",  DNACXDto(barcode, DNACXDto.Risk.MODERATE,DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   IncreasedCov,    NotIncreasedFems, NotIncreasedcfDNA, NotIncreasedIScore), language)),
            Pair("양성2개(ss99,cov)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   IncreasedCov,    NotIncreasedFems, NotIncreasedcfDNA, NotIncreasedIScore), language)),
            Pair("양성2개(ss95,fems)_",  DNACXDto(barcode, DNACXDto.Risk.MODERATE,DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   NotIncreasedCov, IncreasedFems,    NotIncreasedcfDNA, NotIncreasedIScore), language)),
            Pair("양성2개(ss99,fems)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   NotIncreasedCov, IncreasedFems,    NotIncreasedcfDNA, NotIncreasedIScore), language)),
            Pair("양성2개(ss95,cfdna)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   NotIncreasedCov, NotIncreasedFems, IncreasedcfDNA,    NotIncreasedIScore), language)),
            Pair("양성2개(ss99,cfdna)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   NotIncreasedCov, NotIncreasedFems, IncreasedcfDNA,    NotIncreasedIScore), language)),
            Pair("양성2개(cov,cfDNA)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, notInscreasedSS, IncreasedCov,    NotIncreasedFems, IncreasedcfDNA,    NotIncreasedIScore), language)),
            Pair("양성3개(ss95,cov,fems)_",  DNACXDto(barcode, DNACXDto.Risk.MODERATE,DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   IncreasedCov,    IncreasedFems,    NotIncreasedcfDNA, NotIncreasedIScore), language)),
            Pair("양성3개(ss99,cov,fems)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   IncreasedCov,    IncreasedFems,    NotIncreasedcfDNA, NotIncreasedIScore), language)),
            Pair("양성3개(ss95,cov,cfDNA)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   IncreasedCov,    NotIncreasedFems, IncreasedcfDNA,    NotIncreasedIScore), language)),
            Pair("양성3개(ss99,cov,cfDNA)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   IncreasedCov,    NotIncreasedFems, IncreasedcfDNA,    NotIncreasedIScore), language)),
            Pair("양성3개(ss95,cov,iscore)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   IncreasedCov,    NotIncreasedFems, NotIncreasedcfDNA, IncreasedIScore),    language)),
            Pair("양성3개(ss99,cov,iscore)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   IncreasedCov,    NotIncreasedFems, NotIncreasedcfDNA, IncreasedIScore),    language)),
            Pair("양성3개(ss95,fems,cfDNA)_",  DNACXDto(barcode, DNACXDto.Risk.MODERATE,DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   NotIncreasedCov, IncreasedFems,    IncreasedcfDNA,    NotIncreasedIScore), language)),
            Pair("양성3개(ss99,fems,cfDNA)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   NotIncreasedCov, IncreasedFems,    IncreasedcfDNA,    NotIncreasedIScore), language)),
            Pair("양성3개(ss95,fems,iscore)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   NotIncreasedCov, IncreasedFems,    NotIncreasedcfDNA, IncreasedIScore),    language)),
            Pair("양성3개(ss99,fems,iscore)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   NotIncreasedCov, IncreasedFems,    NotIncreasedcfDNA, IncreasedIScore),    language)),
            Pair("양성4개(ss95,cov,fems.cfdna)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   IncreasedCov,    IncreasedFems,    IncreasedcfDNA,    NotIncreasedIScore), language)),
            Pair("양성4개(ss99,cov,fems.cfdna)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   IncreasedCov,    IncreasedFems,    IncreasedcfDNA,    NotIncreasedIScore), language)),
            Pair("양성4개(ss95,cov,fems,iscore)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   IncreasedCov,    IncreasedFems,    NotIncreasedcfDNA, IncreasedIScore),    language)),
            Pair("양성4개(ss99,cov,fems,iscore)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   IncreasedCov,    IncreasedFems,    NotIncreasedcfDNA, IncreasedIScore),    language)),
            Pair("양성4개(ss95,cov,cfdna,iscore)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   IncreasedCov,    NotIncreasedFems, IncreasedcfDNA,    IncreasedIScore),    language)),
            Pair("양성4개(ss99,cov,cfdna,iscore)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   IncreasedCov,    NotIncreasedFems, IncreasedcfDNA,    IncreasedIScore),    language)),
            Pair("양성4개(ss95,fems,cfdna,iscore)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   NotIncreasedCov, IncreasedFems,    IncreasedcfDNA,    IncreasedIScore),    language)),
            Pair("양성4개(ss99,fems,cfdna,iscore)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   NotIncreasedCov, IncreasedFems,    IncreasedcfDNA,    IncreasedIScore),    language)),
            Pair("양성5개(ss95)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS95,   IncreasedCov,    IncreasedFems,    IncreasedcfDNA,    IncreasedIScore),    language)),
            Pair("양성5개(ss99)_",  DNACXDto(barcode, DNACXDto.Risk.HIGH,    DNACXDto.SummaryOfResult(cancer, IncreasedSS99,   IncreasedCov,    IncreasedFems,    IncreasedcfDNA,    IncreasedIScore),    language)),
        )
        for (sor in sors) {
            dto = sor.second
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
            val (cutoff95, cutoff99) = calculateSignalScoreCutOff(age)
            dto.result.signalScore95CutOff = cutoff95
            dto.result.signalScore99CutOff = cutoff99
            dto.comment = comment

            println("./ON204/${sor.first}${dto.risk}.pdf")
            builder(dto).build().save("./ON204/${sor.first}${dto.risk}_${language}.pdf")
        }

    }
    fun build(): PDDocument {
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
        val (cutoff95, cutoff99) = calculateSignalScoreCutOff(age)
        dto.result.signalScore95CutOff = cutoff95
        dto.result.signalScore99CutOff = cutoff99
        dto.comment = comment

        return builder(dto).build()
    }

    private fun builder(dto: DNACXDto): DNACXPageBuilder<*> {
        val doc = PDDocument()
        val page: Painter<DNACXTemplate<DNACXResource>, DNACXDto>
        val sign: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionSign(65f)
        val footer: Painter<DNACXTemplate<DNACXResource>, DNACXDto> = SectionFooterWithLymphotec()
        return when(dto.language) {
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

    private fun calculateSignalScoreCutOff(age: Int): Pair<Double, Double> {
        return if (age<=59) Pair(0.379, 0.614)
        else if (age in 60..69) Pair(0.501, 0.678)
        else Pair(0.581, 0.793)
    }
}

fun main() {
    val test = DNACXON204ReportTest()
//    test.createAllReport()
    test.test()
}
