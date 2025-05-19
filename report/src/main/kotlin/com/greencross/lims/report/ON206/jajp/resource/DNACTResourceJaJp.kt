package com.greencross.lims.report.ON206.jajp.resource

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

abstract class DNACTResourceJaJp(doc: PDDocument): DNACTResource, HasSignEnUs, HasHeaderEnUs {
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
            risk == DNACTDto.Risk.NOT_DETECTED  && !cfDNAIncreased && !iScoreIncreased && !covIncreased && !femsIncreased -> "異常なパターンは検出されませんでした。\n" +
                    "cfDNA濃度、ゲノム不安定性スコア（i-score）、COVスコア、および\n" +
                    "FEMSスコアの上昇は見られませんでした。\n" +
                    "\n" +
                    "がん細胞が存在しないか、または検出限度（0.25％）未満の極めて少量の\n" +
                    "がん細胞が存在する可能性があります。"
            //양성 1개
            risk == DNACTDto.Risk.WEAK          &&  cfDNAIncreased && !iScoreIncreased && !covIncreased && !femsIncreased -> "異常なパターンが検出されました。\n" +
                    "cfDNA濃度が上昇していました。しかし、ゲノム不安定性（i-score）と、\n" +
                    "COVスコアおよびFEMSスコアを含む腫瘍シグナルは上昇していませんでした。\n" +
                    "\n" +
                    "この結果は、残存するがん細胞が少ない、またはがん治療による細胞死が\n" +
                    "原因の可能性があります。引き続き追跡モニタリングを推奨します。"
            risk == DNACTDto.Risk.MODERATE      && !cfDNAIncreased &&  iScoreIncreased && !covIncreased && !femsIncreased -> "異常なパターンが検出されました。\n" +
                    "ゲノム不安定性（i-score）が上昇していました。しかし、cfDNA濃度と、\n" +
                    "COVスコアおよびFEMSスコアを含む腫瘍シグナルは上昇していませんでした。\n" +
                    "\n" +
                    "微量の残存がん細胞が存在する可能性があります。\n" +
                    "引き続き追跡モニタリングを推奨します。"
            risk == DNACTDto.Risk.WEAK          && !cfDNAIncreased && !iScoreIncreased &&  covIncreased && !femsIncreased -> "異常なパターンが検出されました。\n" +
                    "COVスコアが上昇していました。しかし、cfDNA濃度とゲノム不安定性\n" +
                    "（i-score）と、FEMSスコアを含む腫瘍シグナルは上昇していませんでした。\n" +
                    "\n" +
                    "この結果は、残存するがん細胞が少ない、またはがん治療による細胞死が\n" +
                    "原因の可能性があります。引き続き追跡モニタリングを推奨します。"
            risk == DNACTDto.Risk.WEAK          && !cfDNAIncreased && !iScoreIncreased && !covIncreased &&  femsIncreased -> "異常なパターンが検出されました。\n" +
                    "FEMSスコアが上昇していました。しかし、cfDNA濃度とゲノム不安定性\n" +
                    "（i-score）と、COVスコアを含む腫瘍シグナルは上昇していませんでした。\n" +
                    "\n" +
                    "この結果は、残存するがん細胞が少ない、または、がん治療による細胞死が\n" +
                    "原因の可能性があります。引き続き追跡モニタリングを推奨します。"
            //양성 2개
            risk == DNACTDto.Risk.MODERATE      &&  cfDNAIncreased &&  iScoreIncreased && !covIncreased && !femsIncreased -> "異常なパターンが検出されました。\n" +
                    "cfDNA濃度とゲノム不安定性（i-score）が上昇していました。しかし、\n" +
                    "COVスコアおよびFEMSスコアを含む腫瘍シグナルは上昇していませんでした。\n" +
                    "\n" +
                    "少量の残存がん細胞が存在する可能性があります。\n" +
                    "引き続き追跡モニタリングを推奨します。"
            risk == DNACTDto.Risk.WEAK          &&  cfDNAIncreased && !iScoreIncreased &&  covIncreased && !femsIncreased -> "異常なパターンが検出されました。\n" +
                    "cfDNA濃度とCOVスコアが上昇していました。しかし、ゲノム不安定性\n" +
                    "（i-score）と腫瘍シグナル（FEMSスコア）は上昇していませんでした。\n" +
                    "\n" +
                    "この結果は、残存するがん細胞が少ない、またはがん治療による細胞死が\n" +
                    "原因の可能性があります。引き続き追跡モニタリングを推奨します。"
            risk == DNACTDto.Risk.WEAK          &&  cfDNAIncreased && !iScoreIncreased && !covIncreased &&  femsIncreased -> "異常なパターンが検出されました。\n" +
                    "cfDNA濃度とFEMSスコアが上昇していました。しかし、ゲノム不安定性\n" +
                    "（i-score）と腫瘍シグナル（COVスコア）は上昇していませんでした。\n" +
                    "\n" +
                    "この結果は、残存するがん細胞が少ない、または、がん治療による細胞死が\n" +
                    "原因の可能性があります。引き続き追跡モニタリングを推奨します。"
            risk == DNACTDto.Risk.MODERATE      && !cfDNAIncreased &&  iScoreIncreased &&  covIncreased && !femsIncreased -> "異常なパターンが検出されました。\n" +
                    "ゲノム不安定性（i-score）とCOVスコアが上昇していました。しかし、\n" +
                    "cfDNA濃度と腫瘍シグナル（FEMSスコア）は上昇していませんでした。\n" +
                    "\n" +
                    "少量の残存がん細胞が存在する可能性があります。\n" +
                    "引き続き追跡モニタリングを推奨します。"
            risk == DNACTDto.Risk.MODERATE      && !cfDNAIncreased &&  iScoreIncreased && !covIncreased &&  femsIncreased -> "異常なパターンが検出されました。\n" +
                    "ゲノム不安定性（i-score）とFEMSスコアが上昇していました。しかし、\n" +
                    "cfDNA濃度と腫瘍シグナル（COVスコア）は上昇していませんでした。\n" +
                    "\n" +
                    "少量の残存がん細胞が存在する可能性があります。\n" +
                    "引き続き追跡モニタリングを推奨します。"
            risk == DNACTDto.Risk.WEAK          && !cfDNAIncreased && !iScoreIncreased &&  covIncreased &&  femsIncreased -> "異常なパターンが検出されました。\n" +
                    "腫瘍シグナル（COVスコアとFEMSスコア）が上昇していました。しかし、\n" +
                    "cfDNA濃度とゲノム不安定性（i-score）は上昇していませんでした。\n" +
                    "\n" +
                    "この結果は、残存するがん細胞が少ない、またはがん治療による細胞死が\n" +
                    "原因の可能性があります。引き続き追跡モニタリングを推奨します。"
            //양성 3개
            risk == DNACTDto.Risk.MODERATE      &&  cfDNAIncreased &&  iScoreIncreased &&  covIncreased && !femsIncreased -> "異常なパターンが検出されました。\n" +
                    "cfDNA濃度とゲノム不安定性（i-score）とCOVスコアが上昇していました。\n" +
                    "しかし、腫瘍シグナル（FEMSスコア）は上昇していませんでした。\n" +
                    "\n" +
                    "残存がん細胞が存在する可能性があります。\n" +
                    "引き続き追跡モニタリングを推奨します。"
            risk == DNACTDto.Risk.MODERATE      &&  cfDNAIncreased &&  iScoreIncreased && !covIncreased &&  femsIncreased -> "異常なパターンが検出されました。\n" +
                    "cfDNA濃度とゲノム不安定性（i-score）とFEMSスコアが上昇していました。\n" +
                    "しかし、腫瘍シグナル（COVスコア）は上昇していませんでした。\n" +
                    "\n" +
                    "残存がん細胞が存在する可能性があります。\n" +
                    "引き続き追跡モニタリングを推奨します。"
            risk == DNACTDto.Risk.MODERATE      &&  cfDNAIncreased && !iScoreIncreased &&  covIncreased &&  femsIncreased -> "異常なパターンが検出されました。\n" +
                    "cfDNA濃度と腫瘍シグナル（COVスコアとFEMSスコア）が上昇していました。\n" +
                    "しかし、ゲノム不安定性（i-score）は上昇していませんでした。\n" +
                    "\n" +
                    "この結果は、残存するがん細胞が少ない、またはがん治療による細胞死が\n" +
                    "原因の可能性があります。引き続き追跡モニタリングを推奨します。"
            risk == DNACTDto.Risk.MODERATE      && !cfDNAIncreased &&  iScoreIncreased &&  covIncreased &&  femsIncreased -> "異常なパターンが検出されました。\n" +
                    "ゲノム不安定性（i-score）と腫瘍シグナル（COVスコアとFEMSスコア）が\n" +
                    "上昇していました。しかし、cfDNA濃度は上昇していませんでした。\n" +
                    "\n" +
                    "残存がん細胞が存在する可能性があります。\n" +
                    "引き続き追跡モニタリングを推奨します。"
            //양성 4개
            risk == DNACTDto.Risk.STRONG        &&  cfDNAIncreased &&  iScoreIncreased &&  covIncreased &&  femsIncreased -> "異常なパターンが検出されました。\n" +
                    "cfDNA濃度とゲノム不安定性（i-score）が上昇していました。また、\n" +
                    "COVスコアおよびFEMSスコアを含む腫瘍シグナルも上昇しています。\n" +
                    "\n" +
                    "残存がん細胞が存在する可能性が高いです。\n" +
                    "引き続き追跡モニタリングを推奨します。"
            else -> throw Exception("${dto.requestNumber} / 소견생성 불가: 구분되지 않은 증가형 케이스입니다. ")
        }
    }

    override fun imgGenomicInstability(path: String): PDImageXObject = img((if(path != "") File(path) else File(DNACTResource.resource, "img/avoid/SectionGenomicInstabilityPlot/GenomicInstabilityPlot_default.png")))
    override fun imgFEMS(path: String): PDImageXObject = img((if(path != "") File(path) else File(DNACTResource.resource, "img/avoid/SectionGenomicInstabilityPlot/FEMS_default.png")))
    override fun imgFEMSExample(): PDImageXObject = img(File(DNACTResource.resource, "img/avoid/SectionGenomicInstabilityPlot/fems_example_jp.png"))
    override fun imgGenomicInstabilityExample(): PDImageXObject = img(File(DNACTResource.resource, "img/avoid/SectionGenomicInstabilityPlot/genomic_instability_example_jp.png"))
    override fun imgTrackingResultsTable(): PDImageXObject = img(File(DNACTResource.resource, "img/avoid/SectionTrackingResult/table.png"))
    override fun lblInterpretatioon(dto: DNACTDto): String = pragraph(dto)
    override fun imgTrackingGraph(type: String): PDImageXObject = img(File(DNACTResource.resource, "img/avoid/SectionTrackingGraph/${type}_jp.png"))
    override fun colorGray(): Color { return Color.decode("0xEFEFEF") }
    override fun colorText(): Color { return super.colorText()             }
    override fun fontHeaderValue(): PDFont = font(File(DNACTResource.resource, "font/SdGothicNeoRound08.ttf"))
    override fun fontHeader(): PDFont = font(File(DNACTResource.resource, "font/SDGothicNeoRound06.ttf"))
    override fun fontTitle(): PDFont = font(File(DNACTResource.resource, "font/SdGothicNeoRound01.ttf"))
    override fun fontText(): PDFont = font(File(DNACTResource.resource, "font/SdGothicNeoRound04.ttf"))
    override fun fontScientific(): PDFont = font(File(DNACTResource.resource, "font/NanumBarunGothic.ttf"))
    override fun fontContentRegular(): PDFont = font(File(DNACTResource.resource, "font/NotoSansJP-Regular.ttf"))
    override fun fontContentBold():    PDFont = font(File(DNACTResource.resource, "font/NotoSansJP-Bold.ttf"))
    override fun fontContentBoldItalic(): PDFont = font(File(DNACTResource.resource, "font/NotoSansJP-Bold.ttf"))
    override fun styleContentRegualar(): TextStyle = TextStyle().color(Color(35,24, 21)).fontSize(8f).fonts(fontContentRegular())
    override fun styleContentBold(): TextStyle = TextStyle().color(Color(67,72,142)).fontSize(8f).fonts(fontContentBold())
    override fun styleContentBoldIt(): TextStyle = TextStyle().color(Color(67,72,142)).fontSize(8f).fonts(fontContentBoldItalic())
}
