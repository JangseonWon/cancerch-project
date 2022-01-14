package com.greencross.lims.report.avoid.kokr

import com.greencross.lims.report.HasSign.Person
import com.greencross.lims.report.HasSign.SignLabel
import com.greencross.lims.report.Template
import com.greencross.lims.report.TextStyle
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
    val fontHeader:             PDFont
    val fontTitle:              PDFont
    val fontValue:              PDFont
    val fontText:               PDFont
    val fontScientific:         PDFont
    val fontHeaderBoxTitle:     PDFont

    val styleTitle:             TextStyle
    val styleHeaderTitle:       TextStyle

    val imgHeaderBox:           PDImageXObject
    val imgTitle:               PDImageXObject
    val imgContentTitle:        PDImageXObject
    val imgIntroContent:        PDImageXObject
    val imgTotalResultContent:  PDImageXObject
    val imgDoubtSquare:         PDImageXObject
    val imgDoubtContentBox:     PDImageXObject
    val imgDoubtCenterLine:     PDImageXObject
    val imgCancerTypeContent:   PDImageXObject

    init {
        this.doc = doc
        initialize()
        fontHeader          = font(File(AvoidResource.resource, "font/SDGothicNeoRound06.ttf"))
        fontTitle           = font(File(AvoidResource.resource, "font/SdGothicNeoRound01.ttf"))
        fontHeaderBoxTitle  = font(File(AvoidResource.resource, "font/SdGothicNeoRound08.ttf"))
        fontValue           = font(File(AvoidResource.resource, "font/SdGothicNeoRound04.ttf"))
        fontText            = font(File(AvoidResource.resource, "font/NanumBarunGothic.ttf"))
        fontScientific      = font(File(AvoidResource.resource, "font/OpenSans-Regular.ttf"))

        styleTitle          = TextStyle().color(Color(72,71,71)).fontSize(7.4f).fonts(fontTitle)
        styleHeaderTitle    = TextStyle().color(Color(43,48,94)).fontSize(8f).fonts()

        imgTitle            = img(File(AvoidResource.resource, "img/avoid/0_title.png"))
        imgHeaderBox        = img(File(AvoidResource.resource, "img/avoid/1_headerBox.png"))
        imgIntroContent     = img(File(AvoidResource.resource, "img/avoid/2_introcontent.png"))
        imgContentTitle     = img(File(AvoidResource.resource, "img/avoid/C_contentTitle.png"))
        imgTotalResultContent = img(File(AvoidResource.resource, "img/avoid/3_totalresultcontent.png"))
        imgDoubtSquare      = img(File(AvoidResource.resource, "img/avoid/4_doubtsquare.png"))
        imgDoubtContentBox  = img(File(AvoidResource.resource, "img/avoid/5_doubtcontent.png"))
        imgDoubtCenterLine  = img(File(AvoidResource.resource, "img/avoid/6_doubtcenterline.png"))
        imgCancerTypeContent= img(File(AvoidResource.resource, "img/avoid/7_cancertypebox.png"))

    }


    override fun labels(): Array<SignLabel> {
        return arrayOf(
            object : SignLabel {
                override fun label(): String { return "검사자:" }
                override fun persons(template: Template<*>, dto: AbstractReportDto): Array<Person?> {
                    return arrayOf(person("이명근"))
                }
            },
            object : SignLabel {
                override fun label(): String { return "확인자:" }
                override fun persons(template: Template<*>, dto: AbstractReportDto): Array<Person?> {
                    return arrayOf(person("조은해"), person("김영곤"))
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
    override fun fontHeaderValue():     PDFont  { return fontValue          }
    override fun fontHeader():          PDFont  { return fontHeader         }
    override fun fontTitle():           PDFont  { return fontTitle          }
    override fun fontText():            PDFont  { return fontText           }
    override fun fontScientific():      PDFont  { return fontScientific     }
    override fun fontHeaderBoxTitle():  PDFont  { return fontHeaderBoxTitle }
    //endregion

    //region #Override Style
    override fun styleTitle(): TextStyle {
        return styleTitle
    }

    override fun styleHeaderTitle(): TextStyle {
        return styleHeaderTitle
    }
    //endregion

    //region #Override img
    override fun imgTitle():                PDImageXObject { return imgTitle                }
    override fun imgHeaderBox():            PDImageXObject { return imgHeaderBox            }
    override fun imgContentTitle():         PDImageXObject { return imgContentTitle         }
    override fun imgIntroContent():         PDImageXObject { return imgIntroContent         }
    override fun imgTotalResultContent():   PDImageXObject { return imgTotalResultContent   }
    override fun imgDoubtSquare():          PDImageXObject { return imgDoubtSquare          }
    override fun imgDoubtContentBox():      PDImageXObject { return imgDoubtContentBox      }
    override fun imgDoubtCenterLine():      PDImageXObject { return imgDoubtCenterLine      }
    override fun imgCancerTypeContent():    PDImageXObject { return imgCancerTypeContent    }
    //endregion
}