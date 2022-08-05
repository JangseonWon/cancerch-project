package com.greencross.lims.report.avoid.kokr

import com.greencross.lims.report.HasSign.Person
import com.greencross.lims.report.HasSign.SignLabel
import com.greencross.lims.report.Template
import com.greencross.lims.report.TextStyle
import com.greencross.lims.report.avoid.AvoidDto
import com.greencross.lims.report.avoid.AvoidResource
import com.greencross.lims.report.builder.AbstractReportDto
import com.greencross.lims.report.kokr.HasHeaderKoKr
import com.greencross.lims.report.kokr.HasSignKoKr
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color
import java.io.File

abstract class AvoidResourceKoKr(doc: PDDocument): AvoidResource, HasSignKoKr, HasHeaderKoKr {
    var doc: PDDocument = PDDocument()
    val fontHeader              :       PDFont  = font(File(AvoidResource.resource, "font/SDGothicNeoRound06.ttf"))
    val fontTitle               :       PDFont  = font(File(AvoidResource.resource, "font/SdGothicNeoRound01.ttf"))
    val fontValue               :       PDFont  = font(File(AvoidResource.resource, "font/SdGothicNeoRound08.ttf"))
    val fontText                :       PDFont  = font(File(AvoidResource.resource, "font/SdGothicNeoRound04.ttf"))
    val fontScientific          :       PDFont  = font(File(AvoidResource.resource, "font/NanumBarunGothic.ttf"))
    val fontHeaderBoxTitle      :       PDFont  = font(File(AvoidResource.resource, "font/OpenSans-Regular.ttf"))
    val fontContentRegular      :       PDFont  = font(File(AvoidResource.resource, "font/Sandoll_고딕Neo1_TTF_04_Rg.ttf"))
    val fontContentBold         :       PDFont  = font(File(AvoidResource.resource, "font/Sandoll_고딕Neo1_TTF_08_Eb.ttf"))
    val fontSpecial             :       PDFont  = font(File(AvoidResource.resource, "font/Sandoll_격동고딕2_TTF_05_Bd.ttf"))

    val styleContentRegular     :       TextStyle = TextStyle().color(Color(35,24, 21)).fontSize(8f).fonts(fontContentRegular())
    val styleContentBold        :       TextStyle = TextStyle().color(Color(67,72,142)).fontSize(8f).fonts(fontContentBold())
    val styleSpecial            :       TextStyle = TextStyle().fonts(fontSpecial())

    val imgBarNormal            :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/bar_Normal.png"))
    val imgBarGray              :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/bar_Gray.png"))
    val imgBarDanger            :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/bar_Danger.png"))
    val imgDetailResultOverview :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionDetailResultAnalysis/detailResultOverview.png"))
    val imgSmallSquareAverage   :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionDetailResultAnalysis/img_small_square_average.png"))
    val imgSmallSquarePatinet   :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionDetailResultAnalysis/img_small_square_patient.png"))
    val imgAnalysisContentBox   :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionDetailResultAnalysis/contentBox.png"))
    val imgGuideLine            :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionGuideLine/img_table.png"))
    val imgGuideLineToTalCancer :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionGuideLine/img_total.png"))
    val imgProcess              :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionDetailProcess/img_process.png"))
    val imgLBxBox               :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionDetailProcess/img_LBXbox.png"))
    val imgNGS                  :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionDetailProcess/img_ngs.png"))
    val imgLBx                  :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionDetailProcess/img_LBx.png"))
    val imgAIBox                :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionDetailProcess/img_aibox.png"))
    val imgAI                   :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionDetailProcess/img_aiimg.png"))
    val imgMiniSquare           :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionLimitation/img_square.png"))
    val imgLimitationTableTitle :       PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionLimitation/img_table_title.png"))

    override fun labels(): Array<SignLabel> {
        return arrayOf(
            object : SignLabel {
                override fun label(): String { return "검사자:" }
                override fun persons(template: Template<*>, dto: AbstractReportDto): Array<Person?> {
                    return arrayOf(person("김다솜"))
                }
            },
            object : SignLabel {
                override fun label(): String { return "확인자:" }
                override fun persons(template: Template<*>, dto: AbstractReportDto): Array<Person?> {
                    return arrayOf(person("조은해"))
                }
            }
        )
    }

    override fun doc(): PDDocument {
        return doc
    }

    //region #Override Colors
    override fun colorGray(): Color { return Color.decode("0xEFEFEF") }
    override fun colorText(): Color { return super.colorText()             }
    //endregion

    //region #Override Fonts
    override fun fontHeaderValue():         PDFont { return fontValue          }
    override fun fontHeader():              PDFont { return fontHeader         }
    override fun fontTitle():               PDFont { return fontTitle          }
    override fun fontText():                PDFont { return fontText           }
    override fun fontScientific():          PDFont { return fontScientific     }
    override fun fontHeaderBoxTitle():      PDFont { return fontHeaderBoxTitle }
    override fun fontContentRegular():      PDFont { return fontContentRegular }
    override fun fontContentBold():         PDFont { return fontContentBold    }
    override fun fontSpecial():             PDFont { return fontSpecial        }
    //endregion

    //region #Override Style
    override fun styleContentRegualar():    TextStyle { return styleContentRegular       }
    override fun styleContentBold():        TextStyle { return styleContentBold          }
    override fun styleContentSpecial():     TextStyle { return styleSpecial              }
    //endregion

    //region #Override img
    override fun imgTitle()       :          PDImageXObject    = img(File(AvoidResource.resource, "img/avoid/SectionTitle/0_title.png"))
    override fun imgHeaderBox()   :          PDImageXObject    = img(File(AvoidResource.resource, "img/avoid/SectionTitle/1_headerBox.png"))
    override fun imgContentTitle():          PDImageXObject    = img(File(AvoidResource.resource, "img/avoid/C_contentTitle.png"))
    override fun imgIntroContent():          PDImageXObject    = img(File(AvoidResource.resource, "img/avoid/SectionIntro/2_introcontent.png"))

    override fun imgTotalResultContent():    PDImageXObject    = img(File(AvoidResource.resource, "img/avoid/SectionTotalResult/3_totalresultcontent.png"))
    override fun imgTotalResultLowRisk():    PDImageXObject    = img(File(AvoidResource.resource, "img/avoid/SectionTotalResult/KoKr/3_1rowrisk.png"))
    override fun imgTotalResultMiddleRisk(): PDImageXObject    = img(File(AvoidResource.resource, "img/avoid/SectionTotalResult/KoKr/3_2middlerisk.png"))
    override fun imgTotalResultHighRisk():   PDImageXObject    = img(File(AvoidResource.resource, "img/avoid/SectionTotalResult/KoKr/3_3highrisk.png"))
    override fun imgDoubtSquare():           PDImageXObject    = img(File(AvoidResource.resource, "img/avoid/SectionDoubtCancer/4_doubtsquare.png"))
    override fun imgDoubtContentBox():       PDImageXObject    = img(File(AvoidResource.resource, "img/avoid/SectionDoubtCancer/5_doubtcontent.png"))
    override fun imgDoubtCenterLine():       PDImageXObject    = img(File(AvoidResource.resource, "img/avoid/SectionDoubtCancer/6_doubtcenterline.png"))
    override fun imgDoubtCancerPercentages(score: Double): PDImageXObject = img(File(AvoidResource.resource, "img/avoid/SectionDoubtCancer/5_"+(score.toInt()/10).toString()+"_per.png"))
    override fun imgDoubtCancer(name: String): PDImageXObject  = img(File(AvoidResource.resource, "img/avoid/SectionDoubtCancer/6_$name.png"))

    override fun imgRankFirst():            PDImageXObject     = img(File(AvoidResource.resource, "img/avoid/kokr/C_rankfirst.png"))
    override fun imgRankSecond():           PDImageXObject     = img(File(AvoidResource.resource, "img/avoid/kokr/C_ranksecond.png"))

    override fun imgCancerTypeTitle():      PDImageXObject     = img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/large_content_box.png"))
    override fun imgCancerTypeContent():    PDImageXObject     = img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/cancer_box.png"))
    override fun imgCancerTypeImage(cancer: String) : PDImageXObject = img(
        File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/$cancer.png")
    )

    override fun imgCancerTypeDetect():     PDImageXObject     = img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/detected.png"))
    override fun imgUnderBar():             PDImageXObject     = img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/underBar.png"))
    override fun imgCancerReadingGuide():   PDImageXObject     = img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/guidebox.png"))
    override fun imgHuman():                PDImageXObject     = img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/human.png"))
    override fun imgEsop(first: String): PDImageXObject {
        return img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/식도암_"+
            (first == "식도암")+".png"))
    }
    override fun imgLung(first: String): PDImageXObject {
        return img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/폐암_"+
                (first == "폐암")+".png"))
    }
    override fun imgLiver(first: String): PDImageXObject {
        return img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/간암_"+
                (first == "간암")+".png"))
    }
    override fun imgPanc(first: String): PDImageXObject {
        return img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/췌장암_"+
                (first == "췌장암")+".png"))
    }
    override fun imgColon(first: String): PDImageXObject {
        return img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/대장암_"+
                (first == "대장암")+".png"))
    }
    override fun imgBreast(first: String): PDImageXObject {
        return img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/유방암_"+
                (first == "유방암")+".png"))
    }
    override fun imgOvary(first: String): PDImageXObject {
        return img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/난소암_"+
                (first == "난소암")+".png"))
    }

    override fun imgLine(cancer: String): PDImageXObject {
        return img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/line_$cancer.png"))
    }

    override fun imgDetailResultOverview(): PDImageXObject { return imgDetailResultOverview }
    override fun imgDetailResultTable(result: AvoidDto.Results): PDImageXObject = when(result){
        AvoidDto.Results.RISK    -> img(File(AvoidResource.resource, "img/avoid/SectionDetailResultAnalysis/detailResultLayout_CONCENT.png"))
        AvoidDto.Results.CONCERN  -> img(File(AvoidResource.resource, "img/avoid/SectionDetailResultAnalysis/detailResultLayout_ATTENTION.png"))
        else                        -> img(File(AvoidResource.resource, "img/avoid/SectionDetailResultAnalysis/detailResultLayout_NORMAL.png"))
    }

    override fun imgDetailResultRisk(risk: String): PDImageXObject {
        return img(File(AvoidResource.resource, "img/avoid/SectionDetailResultAnalysis/img_$risk.png"))
    }
    override fun imgAnalysisContentBox():   PDImageXObject { return imgAnalysisContentBox }

    override fun imgSmallSquarePatient():   PDImageXObject { return imgSmallSquarePatinet }
    override fun imgSmallSquareAverage():   PDImageXObject { return imgSmallSquareAverage }

    override fun imgBackgroundCancer(cancer: String): PDImageXObject {
        return img(File(AvoidResource.resource, "img/avoid/SectionDetailResultAnalysis/background_$cancer.png"))
    }

    override fun imgGuideLineTable():       PDImageXObject { return imgGuideLine   }
    override fun imgGuideLineCancer(cancer: String): PDImageXObject {
        return img(File(AvoidResource.resource, "img/avoid/SectionGuideLine/cancer_$cancer.png"))
    }
    override fun imgGuideLineTotalCancer() : PDImageXObject{ return imgGuideLineToTalCancer }
    override fun imgProcess():               PDImageXObject{ return imgProcess }
    override fun imgAi():                    PDImageXObject { return imgAI }
    override fun imgAiBox():                 PDImageXObject { return imgAIBox }
    override fun imgLBx():                   PDImageXObject { return imgLBx }
    override fun imgLBxBox():                PDImageXObject { return imgLBxBox }
    override fun imgNGS():                   PDImageXObject { return imgNGS }
    override fun imgLimitationTableTitle():  PDImageXObject { return imgLimitationTableTitle }
    override fun imgLimitationTable1():      PDImageXObject = img(File(AvoidResource.resource, "img/avoid/SectionLimitation/img_limitation_table1.png"))
    override fun imgLimitationTable2():      PDImageXObject = img(File(AvoidResource.resource, "img/avoid/SectionLimitation/img_limitation_table2.png"))
    override fun imgReferenceVertical():     PDImageXObject = img(File(AvoidResource.resource, "img/avoid/SectionLimitation/img_reference_vertical.png"))
    override fun imgReferenceHorizontal():   PDImageXObject = img(File(AvoidResource.resource, "img/avoid/SectionLimitation/img_reference_horizontal.png"))
    override fun imgBarNormal():             PDImageXObject { return imgBarNormal    }
    override fun imgBarGray():               PDImageXObject { return imgBarGray      }
    override fun imgBarMiddle():             PDImageXObject = img(File(AvoidResource.resource, "img/avoid/SectionCancerTypeDanger/bar_Middle.png"))
    override fun imgBarDanger():             PDImageXObject { return imgBarDanger    }
    override fun imgMiniSquare():            PDImageXObject { return imgMiniSquare  }

    //endregion
}