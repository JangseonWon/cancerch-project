package com.greencross.lims.report.ON206.enus.resource

import com.gcgenome.lims.report.Template
import com.gcgenome.lims.report.TextStyle
import com.greencross.lims.report.HasSign
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.builder.AbstractReportDto
import com.greencross.lims.report.enus.HasHeaderEnUs
import com.greencross.lims.report.enus.HasSignEnUs
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color
import java.io.File

abstract class DNACTResourceEnUs(doc: PDDocument): DNACTResource, HasSignEnUs, HasHeaderEnUs {
    var doc                     : PDDocument = PDDocument()

    override fun doc(): PDDocument {
        return doc
    }

    override fun imgSummaryOfResultsTable(): PDImageXObject = img(File(DNACTResource.resource, "img/avoid/SectionSummaryOfResults/table_ct.png"))

    //검사자/전문의 서명
    override fun labels(): Array<HasSign.SignLabel> {
        return arrayOf(
            object : HasSign.SignLabel {
                override fun label(): String { return "Tested By: " }
                override fun persons(template: Template<*>, dto: AbstractReportDto): Array<HasSign.Person?> {
                    return arrayOf(person("김다솜"))
                }
            },
            object : HasSign.SignLabel {
                override fun label(): String { return "Reported by/Reviewed by: " }
                override fun persons(template: Template<*>, dto: AbstractReportDto): Array<HasSign.Person?> {
                    return arrayOf(person("조은해"), person("설창안"))
                }
            }
        )
    }

    private fun pragraph(dto: DNACTDto): String {
        val risk = dto.risk
        val cfDNAIncreased = dto.result[0].cfDNAConcentrationCutOff <= dto.result[0].cfDNAConcentration
        val iScoreIncreased = dto.result[0].genomicInstabilityCutOff <= dto.result[0].genomicInstability
        val covIncreased = dto.result[0].covScoreCutOff <= dto.result[0].covScore
        val femsIncreased = dto.result[0].femsScoreCutOff <= dto.result[0].femsScore
        return when {
            //양성 0개
            risk == DNACTDto.Risk.NOT_DETECTED  && !cfDNAIncreased && !iScoreIncreased && !covIncreased && !femsIncreased -> "No abnormal patterns were detected in the DNA CT: cfDNA concentration, genomic instability score (i-score), COV score, and FEMS score were not increased. \n" + "Based on these results, the patient is unlikely to have a substantial amount of cancer cells or may have a tumor fraction below the detection limit (less than 0.25%)."
            //양성 1개
            risk == DNACTDto.Risk.WEAK          &&  cfDNAIncreased && !iScoreIncreased && !covIncreased && !femsIncreased -> "Abnormal patterns were detected in the DNA CT: cfDNA concentration was increased. However, genomic instability (i-score) was not increased, and the probabilities of tumor signals, including COV and FEMS scores, were also not increased. \n" + "These results may be due to a low number of residual cancer cells or treatment-related cell death. A follow-up test is recommended."
            risk == DNACTDto.Risk.MODERATE      && !cfDNAIncreased &&  iScoreIncreased && !covIncreased && !femsIncreased -> "Abnormal patterns were detected in the DNA CT: genomic instability (i-score) was increased. However, cfDNA concentration was not increased, and the probabilities of tumor signals, including COV and FEMS scores, were also not increased. \n" + "Based on these results, the patient may have a very small number of residual cancer cells. A follow-up test is recommended."
            risk == DNACTDto.Risk.WEAK          && !cfDNAIncreased && !iScoreIncreased &&  covIncreased && !femsIncreased -> "Abnormal patterns were detected in the DNA CT: COV score was increased. However, genomic instability (i-score) and cfDNA concentration were not increased, and the other probability of tumor signal (FEMS scores) was also not increased. \n" + "These results may be due to a low number of residual cancer cells or treatment-related cell death. A follow-up test is recommended."
            risk == DNACTDto.Risk.WEAK          && !cfDNAIncreased && !iScoreIncreased && !covIncreased &&  femsIncreased -> "Abnormal patterns were detected in the DNA CT: FEMS score was increased. However, cfDNA concentration and genomic instability (i-score) were not increased, and the other probability of tumor signal (COV scores) was also not increased. \n" + "These results may be due to a low number of residual cancer cells or treatment-related cell death. A follow-up test is recommended."
            //양성 2개
            risk == DNACTDto.Risk.MODERATE      &&  cfDNAIncreased &&  iScoreIncreased && !covIncreased && !femsIncreased -> "Abnormal patterns were detected in the DNA CT: cfDNA concentration and genomic instability (i-score) were increased. However, the probabilities of tumor signals, including COV and FEMS scores, were not increased. \n" + "Based on these results, the patient may have a small number of residual cancer cells. A follow-up test is recommended."
            risk == DNACTDto.Risk.WEAK          &&  cfDNAIncreased && !iScoreIncreased &&  covIncreased && !femsIncreased -> "Abnormal patterns were detected in the DNA CT: cfDNA concentration and COV score were increased. However, genomic instability (i-score) and the probabilities of tumor signal (FEMS score) were not increased.\n" + "These results may be due to a low number of residual cancer cells or treatment-related cell death. A follow-up test is recommended."
            risk == DNACTDto.Risk.WEAK          &&  cfDNAIncreased && !iScoreIncreased && !covIncreased &&  femsIncreased -> "Abnormal patterns were detected in the DNA CT: cfDNA concentration and FEMS score were increased. However, genomic instability (i-score) and the probabilities of tumor signal (COV score) were not increased.\n" + "These results may be due to a low number of residual cancer cells or treatment-related cell death. A follow-up test is recommended."
            risk == DNACTDto.Risk.MODERATE      && !cfDNAIncreased &&  iScoreIncreased &&  covIncreased && !femsIncreased -> "Abnormal patterns were detected in the DNA CT: genomic instability (i-score) and COV score were increased. However, cfDNA concentration and the probabilities of tumor signal (FEMS score) were not increased.\n" + "Based on these results, the patient may have a small number of residual cancer cells. A follow-up test is recommended."
            risk == DNACTDto.Risk.MODERATE      && !cfDNAIncreased &&  iScoreIncreased && !covIncreased &&  femsIncreased -> "Abnormal patterns were detected in the DNA CT: genomic instability (i-score) and FEMS score were increased. However, cfDNA concentration and the probabilities of tumor signal (COV score) were not increased.\n" + "Based on these results, the patient may have a small number of residual cancer cells. A follow-up test is recommended."
            risk == DNACTDto.Risk.WEAK          && !cfDNAIncreased && !iScoreIncreased &&  covIncreased &&  femsIncreased -> "Abnormal patterns were detected in the DNA CT: the probabilities of tumor signals (COV and FEMS scores) were increased. However, cfDNA concentration and genomic instability (i-score) were not increased.\n" + "These results may be due to a low number of residual cancer cells or treatment-related cell death. A follow-up test is recommended."
            //양성 3개
            risk == DNACTDto.Risk.MODERATE      &&  cfDNAIncreased &&  iScoreIncreased &&  covIncreased && !femsIncreased -> "Abnormal patterns were detected in the DNA CT: cfDNA concentration, genomic instability (i-score), and COV score were increased. However, the probability of tumor signal (FEMS score) was not increased.\n" + "Based on these results, the patient may have residual cancer cells. A follow-up test is recommended."
            risk == DNACTDto.Risk.MODERATE      &&  cfDNAIncreased &&  iScoreIncreased && !covIncreased &&  femsIncreased -> "Abnormal patterns were detected in the DNA CT: cfDNA concentration, genomic instability (i-score), and FEMS score were increased. However, the probability of tumor signal (COV score) was not increased.\n" + "Based on these results, the patient may have residual cancer cells. A follow-up test is recommended."
            risk == DNACTDto.Risk.MODERATE      &&  cfDNAIncreased && !iScoreIncreased &&  covIncreased &&  femsIncreased -> "Abnormal patterns were detected in the DNA CT: cfDNA concentration and the probabilities of tumor signal (COV and FEMS scores) were increased. However, genomic instability (i-score) was not increased.\n" + "These results may be due to a low number of residual cancer cells or treatment-related cell death. A follow-up test is recommended."
            risk == DNACTDto.Risk.MODERATE      && !cfDNAIncreased &&  iScoreIncreased &&  covIncreased &&  femsIncreased -> "Abnormal patterns were detected in the DNA CT: genomic instability (i-score) and the probabilities of tumor signal (COV and FEMS scores) were increased. However, the cfDNA concentration was not increased.\n" + "Based on these results, the patient may have residual cancer cells. A follow-up test is recommended."
            //양성 4개
            risk == DNACTDto.Risk.STRONG        &&  cfDNAIncreased &&  iScoreIncreased &&  covIncreased &&  femsIncreased -> "Abnormal patterns were detected in the DNA CT, which shows increased cfDNA concentration and high genomic instability (i-score). The probabilities of tumor signal including COV and FEMS scores were also increased.\n" + "Based on these results, it is very likely that the patient has residual cancer cells. A follow-up test is recommended."
            else -> throw Exception("${dto.requestNumber} / 소견생성 불가: 구분되지 않은 증가형 케이스입니다. ")
        }
    }

    override fun imgGenomicInstability(path: String): PDImageXObject = img(if(path != "") File(path) else File(DNACTResource.resource, "img/avoid/SectionGenomicInstabilityPlot/GenomicInstabilityPlot_default.png"))
    override fun imgFEMS(path: String): PDImageXObject = img(if(path != "") File(path) else File(DNACTResource.resource, "img/avoid/SectionGenomicInstabilityPlot/FEMS_default.png"))
    override fun imgFEMSExample(): PDImageXObject = img(File(DNACTResource.resource, "img/avoid/SectionGenomicInstabilityPlot/fems_example_en.png"))
    override fun imgGenomicInstabilityExample(): PDImageXObject = img(File(DNACTResource.resource, "img/avoid/SectionGenomicInstabilityPlot/genomic_instability_example_en.png"))
    override fun imgTrackingResultsTable(): PDImageXObject = img(File(DNACTResource.resource, "img/avoid/SectionTrackingResult/table.png"))
    override fun lblInterpretatioon(dto: DNACTDto): String = pragraph(dto)
    override fun imgTrackingGraph(type: String): PDImageXObject = img(File(DNACTResource.resource, "img/avoid/SectionTrackingGraph/${type}_en.png"))
    override fun colorGray(): Color { return Color.decode("0xEFEFEF") }
    override fun colorText(): Color { return super.colorText()             }
    override fun fontHeaderValue(): PDFont = font(File(DNACTResource.resource, "font/SdGothicNeoRound08.ttf"))
    override fun fontHeader(): PDFont = font(File(DNACTResource.resource, "font/SDGothicNeoRound06.ttf"))
    override fun fontTitle(): PDFont = font(File(DNACTResource.resource, "font/SdGothicNeoRound01.ttf"))
    override fun fontText(): PDFont = font(File(DNACTResource.resource, "font/SdGothicNeoRound04.ttf"))
    override fun fontScientific(): PDFont = font(File(DNACTResource.resource, "font/NanumBarunGothic.ttf"))
    override fun fontContentRegular(): PDFont = font(File(DNACTResource.resource, "font/MyriadPro-Regular.ttf"))
    override fun fontContentBold(): PDFont = font(File(DNACTResource.resource, "font/MyriadPro-Bold.ttf"))
    override fun fontContentBoldItalic(): PDFont = font(File(DNACTResource.resource, "font/MyriadPro-BoldIt.ttf"))
    override fun styleContentRegualar(): TextStyle = TextStyle().color(Color(35,24, 21)).fontSize(8f).fonts(fontContentRegular())
    override fun styleContentBold(): TextStyle = TextStyle().color(Color(67,72,142)).fontSize(8f).fonts(fontContentBold())
    override fun styleContentBoldIt(): TextStyle = TextStyle().color(Color(67,72,142)).fontSize(8f).fonts(fontContentBoldItalic())
}
