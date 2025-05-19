package com.greencross.lims.report.ON204.jajp.resource

import com.gcgenome.lims.report.Template
import com.gcgenome.lims.report.TextStyle
import com.greencross.lims.report.ON204.DNACXDto
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.HasSign
import com.greencross.lims.report.builder.AbstractReportDto
import com.greencross.lims.report.enus.HasHeaderEnUs
import com.greencross.lims.report.enus.HasSignEnUs
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color
import java.io.File

abstract class DNACXResourceJaJp(doc: PDDocument): DNACXResource, HasSignEnUs, HasHeaderEnUs {
    var doc                     : PDDocument = PDDocument()

    override fun doc(): PDDocument {
        return doc
    }
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

    //이미지 로드
    override fun imgHuman(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionTissueOfOrigin/human.png"))
    override fun imgHumanBox(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionTissueOfOrigin/humanBox.png"))
    override fun imgSummaryOfResults(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionSummaryOfResults/table.png"))
    override fun imgCangerTypeIcon(cancer: String, type: Boolean): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionTissueOfOrigin/${cancer}_${type}.png"))
    override fun imgCancerBox(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionTissueOfOrigin/cancerBox.png"))
    override fun imgCancerTypeImage(cancer: String): PDImageXObject  = img(File(DNACXResource.resource, "img/avoid/SectionTissueOfOrigin/${cancer}.png"))
    override fun lblCancerName(cancer: String): String = when(cancer) {
        "폐암" -> "肺がん"
        "간암" -> "肝臓がん"
        "대장암" -> "大腸がん"
        "식도암" -> "食道がん"
        "췌장담도암" -> "膵臓がん"
        "난소암" -> "卵巣がん"
        else -> throw Exception("정의되지 않은 암종이 등록됐음")
    }

    private fun toCancerNameWithoutCancer(cancer: String): String = when(cancer) {
        "폐암" -> "肺"
        "간암" -> "肝臓"
        "대장암" -> "大腸"
        "식도암" -> "食道"
        "췌장담도암" -> "膵臓"
        "난소암" -> "卵巣"
        else -> throw Exception("정의되지 않은 암종이 등록됐음")
    }

    override fun lblInterpretationBold(dto: DNACXDto): String = when {
        dto.risk == DNACXDto.Risk.HIGH && dto.result.signalScore99CutOff <= dto.result.signalScore -> "検査の結果、あなたは高リスクで、[ ${toCancerNameWithoutCancer(dto.result.cancer)} ]がん患者と非常に類似するDNAパターンを\n示しています。"
        dto.risk == DNACXDto.Risk.HIGH -> "検査の結果、あなたは高リスクで、がん患者と非常に類似するDNAパターンを\n示しています。"
        dto.risk == DNACXDto.Risk.MODERATE -> "検査の結果、あなたは中リスクで、がん患者と類似するDNAパターンを\n" + "示しています。"
        dto.risk == DNACXDto.Risk.MILD -> "検査の結果、あなたは低リスクで、健常人と多少異なるDNAパターンを\n示しています。"
        dto.risk == DNACXDto.Risk.LOW -> "検査の結果、あなたはほとんど疑いなしで、健常人と類似したDNAパターンを\n示しています。"
        else -> throw Exception("소견 자동생성 불가")
    }
    private fun buildParagraph(risk: DNACXDto.Risk, ssIsIncreased95: Boolean, ssIsIncreased99: Boolean, covIsIncreased: Boolean, femsIsIncreased: Boolean, cfDNAIsIncreased: Boolean, genomicIsIncreased: Boolean): String {
        return when {
            //양성 0개
            risk == DNACXDto.Risk.LOW       && !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンは検出されませんでした。\n" +
                    "シグナルスコア、COVスコア、FEMSスコア、cfDNA濃度、および\n" +
                    "ゲノム不安定性スコア（i-score）に特筆すべき上昇は見られませんでした。\n" +
                    "人工知能アルゴリズムを利用して実施したcfDNA分析に基づくと、この結果はがんの\n" +
                    "存在の可能性が低いことを示しています。\n" +
                    "\n" +
                    "本検査はすべての種類のがんを検出できるわけではなく、がんの種類や\n" +
                    "ステージによって検出性能が異なる場合があります。\n" +
                    "\n" +
                    "本検査はがんスクリーニング検査であり、確定診断検査ではないため、\n" +
                    "がん確定診断のためには担当医とご相談ください。"
            //양성 1개
            risk == DNACXDto.Risk.MODERATE  &&  ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアが基準値を超えるスコアを示しました。しかし、腫瘍シグナル\n" +
                    "（COVスコア、FEMSスコア）とcfDNA濃度、ゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "中リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により\n" +
                    "中リスクに分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアが基準値を超えるスコアを示しました。しかし、腫瘍シグナル\n" +
                    "（COVスコア、FEMSスコア）とcfDNA濃度、ゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.MILD      && !ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "COVスコアが基準値を超えるスコアを示しました。しかし、cfDNA濃度と\n" +
                    "ゲノム不安定性（i-score）、腫瘍シグナル（シグナルスコア、FEMSスコア）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "人工知能アルゴリズムを利用して実施したcfDNA分析に基づくと、\n" +
                    "この結果は健常人とはわずかに異なるDNAパターンを示しています。\n" +
                    "\n" +
                    "この結果は採血時の健康状態に基づいて分析したものです。\n" +
                    "引き続き追跡モニタリングを推奨します。"
            risk == DNACXDto.Risk.MILD      && !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "FEMSスコアが基準値を超えるスコアを示しました。しかし、cfDNA濃度と\n" +
                    "ゲノム不安定性（i-score）、腫瘍シグナル（シグナルスコア、COVスコア）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "人工知能アルゴリズムを利用して実施したcfDNA分析に基づくと、\n" +
                    "この結果は健常人とはわずかに異なるDNAパターンを示しています。\n" +
                    "\n" +
                    "この結果は採血時の健康状態に基づいて分析したものです。\n" +
                    "引き続き追跡モニタリングを推奨します。"
            risk == DNACXDto.Risk.MILD      && !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "cfDNA濃度が基準値を超えるスコアを示しました。しかし、腫瘍シグナル\n" +
                    "（シグナルスコア、COVスコア、FEMSスコア）とゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "人工知能アルゴリズムを利用して実施したcfDNA分析に基づくと、\n" +
                    "この結果は健常人とはわずかに異なるDNAパターンを示しています。\n" +
                    "\n" +
                    "この結果は採血時の健康状態に基づいて分析したものです。\n" +
                    "引き続き追跡モニタリングを推奨します。"
            risk == DNACXDto.Risk.MILD      && !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "ゲノム不安定性（i-score）が基準値を超えるスコアを示しました。しかし、\n" +
                    "cfDNA濃度と腫瘍シグナル（シグナルスコア、COVスコア、FEMSスコア）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "人工知能アルゴリズムを利用して実施したcfDNA分析に基づくと、\n" +
                    "この結果は健常人とはわずかに異なるDNAパターンを示しています。\n" +
                    "\n" +
                    "この結果は採血時の健康状態に基づいて分析したものです。\n" +
                    "引き続き追跡モニタリングを推奨します。"
            //양성 2개
            risk == DNACXDto.Risk.MODERATE  &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコアが基準値を超えるスコアを示しました。\n" +
                    "しかし、腫瘍シグナル（FEMSスコア）とcfDNA濃度、ゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "中リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により中リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコアが基準値を超えるスコアを示しました。しかし、\n" +
                    "腫瘍シグナル（FEMSスコア）とcfDNA濃度、ゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.MODERATE  &&  ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとFEMSスコアが基準値を超えるスコアを示しました。しかし、\n" +
                    "腫瘍シグナル（COVスコア）とcfDNA濃度、ゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "中リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により中リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとFEMSスコアが基準値を超えるスコアを示しました。しかし、\n" +
                    "腫瘍シグナル（COVスコア）とcfDNA濃度、ゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとcfDNA濃度が基準値を超えるスコアを示しました。しかし、\n" +
                    "腫瘍シグナル（COVスコアとFEMSスコア）とゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとcfDNA濃度が基準値を超えるスコアを示しました。しかし、\n" +
                    "腫瘍シグナル（COVスコアとFEMSスコア）とゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
                                                ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                                ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                               !ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
            risk == DNACXDto.Risk.HIGH      && !ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "COVスコアとcfDNA濃度が基準値を超えるスコアを示しました。しかし、\n" +
                    "腫瘍シグナル（シグナルスコアとFEMSスコア）とゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
                                               !ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                               !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                               !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                               !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
            //양성 3개
            risk == DNACXDto.Risk.MODERATE  &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、FEMSスコアが基準値を超えるスコアを示しました。\n" +
                    "しかし、cfDNA濃度とゲノム不安定性（i-score）は基準値内のスコアでした。\n" +
                    "\n" +
                    "中リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により中リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、FEMSスコアが基準値を超えるスコアを示しました。\n" +
                    "しかし、cfDNA濃度とゲノム不安定性（i-score）は基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、cfDNA濃度が基準値を超えるスコアを示しました。\n" +
                    "しかし、腫瘍シグナル（FEMSスコア）とゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、cfDNA濃度が基準値を超えるスコアを示しました。\n" +
                    "しかし、腫瘍シグナル（FEMSスコア）とゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、ゲノム不安定性（i-score）が基準値を超える\n" +
                    "スコアを示しました。しかし、腫瘍シグナル（FEMSスコア）とcfDNA濃度は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、ゲノム不安定性（i-score）が基準値を超える\n" +
                    "スコアを示しました。しかし、腫瘍シグナル（FEMSスコア）とcfDNA濃度は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.MODERATE  &&  ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとFEMSスコア、cfDNA濃度が基準値を超えるスコアを示しました。\n" +
                    "しかし、腫瘍シグナル（COVスコア）、ゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "中リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により中リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとFEMSスコア、cfDNA濃度が基準値を超えるスコアを示しました。\n" +
                    "しかし、腫瘍シグナル（COVスコア）、ゲノム不安定性（i-score）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとFEMSスコア、ゲノム不安定性（i-score）が基準値を超える\n" +
                    "スコアを示しました。しかし、cfDNA濃度と腫瘍シグナル（COVスコア）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとFEMSスコア、ゲノム不安定性（i-score）が基準値を超える\n" +
                    "スコアを示しました。しかし、cfDNA濃度と腫瘍シグナル（COVスコア）は\n" +
                    "基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
                                                ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                                ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                                ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                                ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                               !ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                               !ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
            //양성 4개
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、FEMSスコア、cfDNA濃度が基準値を超えるスコアを\n" +
                    "示しました。しかし、ゲノム不安定性（i-score）は基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、FEMSスコア、cfDNA濃度が基準値を超えるスコアを\n" +
                    "示しました。しかし、ゲノム不安定性（i-score）は基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、FEMSスコア、ゲノム不安定性（i-score）が\n" +
                    "基準値を超えるスコアを示しました。しかし、cfDNA濃度は基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、FEMSスコア、ゲノム不安定性（i-score）が\n" +
                    "基準値を超えるスコアを示しました。しかし、cfDNA濃度は基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、ゲノム不安定性（i-score）、cfDNA濃度が基準値を\n" +
                    "超えるスコアを示しました。しかしFEMSスコアは基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、ゲノム不安定性（i-score）、cfDNA濃度が基準値を\n" +
                    "超えるスコアを示しました。しかしFEMSスコアは基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとFEMSスコア、cfDNA濃度、ゲノム不安定性（i-score）が基準値を\n" +
                    "超えるスコアを示しました。しかし、COVスコアは基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとFEMSスコア、cfDNA濃度、ゲノム不安定性（i-score）が基準値を\n" +
                    "超えるスコアを示しました。しかし、COVスコアは基準値内のスコアでした。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
                                               !ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
            //양성 5개
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、FEMSスコア、cfDNA濃度、ゲノム不安定性（i-score）\n" +
                    "が基準値を超えるスコアを示しました。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の5％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> "異常なパターンが検出されました。\n" +
                    "シグナルスコアとCOVスコア、FEMSスコア、cfDNA濃度、ゲノム不安定性（i-score）\n" +
                    "が基準値を超えるスコアを示しました。\n" +
                    "\n" +
                    "高リスクの結果ががんの確定診断を意味するものではありません。\n" +
                    "健常人の1％が健康状態（例：良性疾患、自己免疫疾患）により高リスクに\n" +
                    "分類されることがあります。\n" +
                    "\n" +
                    "※確定診断もしくはモニタリングのため医師と相談することを推奨します。"
            else -> throw Exception("분류되지 않은 케이스로 소견 문단 2번 생성이 불가능합니다.")
        }
    }
    override fun lblInterpretationRegular(dto: DNACXDto): String {
        val ScoreParagraph = buildParagraph(
            dto.risk,
            dto.result.signalScore >= dto.result.signalScore95CutOff,
            dto.result.signalScore >= dto.result.signalScore99CutOff,
            dto.result.covScore >= dto.result.covScoreCutOff,
            dto.result.femsScore >= dto.result.femsScoreCutOff,
            dto.result.cfDNAConcentration >= dto.result.cfDNAConcentrationCutOff,
            dto.result.genomicInstability >= dto.result.genomicInstabilityCutOff)
        return ScoreParagraph
    }

    override fun imgSignalScoreBackground(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionScores/signal_jp.png"))
    override fun imgShortSmallTitle(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionScores/shortSmallTitle.png"))
    override fun imgLongSmallTitle(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionScores/LongSmallTitle.png"))
    override fun imgCovScoreBackgorund(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionScores/cov_jp.png"))
    override fun imgFemsScoreBackgorund(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionScores/fems_jp.png"))
    override fun imgGenomicInstabilityScoreBackgorund(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionScores/iscore_jp.png"))
    //region #Override Colors
    override fun colorGray(): Color { return Color.decode("0xEFEFEF") }
    override fun colorText(): Color { return super.colorText()             }
    //endregion

    //region #Override Fonts
    override fun fontHeaderValue():         PDFont = font(File(DNACXResource.resource, "font/SdGothicNeoRound08.ttf"))
    override fun fontHeader():              PDFont = font(File(DNACXResource.resource, "font/SDGothicNeoRound06.ttf"))
    override fun fontTitle():               PDFont = font(File(DNACXResource.resource, "font/SdGothicNeoRound01.ttf"))
    override fun fontText():                PDFont = font(File(DNACXResource.resource, "font/SdGothicNeoRound04.ttf"))
    override fun fontScientific():          PDFont = font(File(DNACXResource.resource, "font/NanumBarunGothic.ttf"))
    override fun fontContentRegular():      PDFont = font(File(DNACXResource.resource, "font/NotoSansJP-Regular.ttf"))
    override fun fontContentBold():         PDFont = font(File(DNACXResource.resource, "font/NotoSansJP-Bold.ttf"))
    override fun fontSpecial():             PDFont = font(File(DNACXResource.resource, "font/Sandoll_격동고딕2_TTF_05_Bd.ttf"))
    override fun styleContentRegualar():    TextStyle = TextStyle().color(Color(35,24, 21)).fontSize(8f).fonts(fontContentRegular())
    override fun styleContentBold():        TextStyle = TextStyle().color(Color(67,72,142)).fontSize(8f).fonts(fontContentBold())
    override fun styleContentSpecial():     TextStyle = TextStyle().fonts(fontSpecial())
    //endregion

}
