package com.greencross.lims.report.cancerch.GangbukComponent

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.builder.Util_EnUS.Companion.cancerToEng
import com.greencross.lims.report.builder.Util_EnUS.Companion.lblPatientInfo
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.cancerch.CancerchResource
import com.greencross.lims.report.cancerch.CancerchTemplate
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.AlignVertical
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class GangbukSectionAnalysisEnUs(private val y: Float = 200f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.line(35f, y+113, 560f, y+113).setStrokingColor(Color(67, 72, 142)).setLineWidth(1f).stroke()
        stream.line(35f, y+67, 560f, y+67).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
        stream.line(35f, y-98, 560f, y-98).setStrokingColor(Color(67, 72, 142)).setLineWidth(1f).stroke()
        stream.line(195f, y+113, 195f, y-98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()

        stream.paragraph(113f,y +85,200f,AlignHorizontal.CENTER,TextBlock(template!!.resource().styleContentRegualar().clone().color(Color(0, 0, 0)).fontSize(12f), "Suspected Cancer Type"))

        var styleBold = template.resource().styleContentBold().clone().fontSize(26f)
        if (dto!!.result == CancerchDto.Results.RISK) {
            if (dto.first.name == "기타암종") {
                stream.line(375f, y + 67, 375f, y - 98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
                stream.paragraph(365f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(13f), cancerToEng(dto.first.name)))
            } else {
                stream.line(375f, y + 113, 375f, y - 98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
                stream.paragraph(290f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.fontSize(13f), cancerToEng(dto.first.name)))
                stream.paragraph(468f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.fontSize(13f), "Others"))
            }
        } else if (dto.result == CancerchDto.Results.CONCERN) {
            stream.line(375f, y + 67, 375f, y - 98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
            stream.paragraph(375f, y + 85, 400f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(13f),  "Not Specified : Follow-up is recommended"))
        } else {
            stream.paragraph(375f, y + 85, 400f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(13f),  "N/A"))
        }

        var styleRegular = template.resource().styleContentRegualar().clone().fontSize(6f)
        if (dto.result != CancerchDto.Results.GENERAL) {
            stream.rect(45f, y-27f, 138f, 53f).setNonStrokingColor(Color(242,242,247)).fill()
            if(dto.result == CancerchDto.Results.RISK) stream.rect(56f, y-40f, 10f, 10f).setNonStrokingColor(Color(217,  52, 29)).fill()
            else stream.rect(56f, y-40.5f, 10f, 10f).setNonStrokingColor(Color(239, 167, 24)).fill()
            stream.rect(56f, y-59f, 10f, 10f).setNonStrokingColor(Color(108,109,112)).fill()
            stream.paragraph(72f, y - 45, 110f, AlignHorizontal.LEFT, AlignVertical.MIDDLE, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(8f), dto.patientName!!))
            stream.paragraph(71f, y - 67, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(8f), lblPatientInfo(dto.age!!, dto.sex!!)))
            stream.paragraph(113f, y, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "Risk comparison"))
        } else {
            stream.paragraph(113f, y-20, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "Risk comparison"))
        }

        styleRegular = template.resource().styleContentRegualar().clone().fontSize(6f)
        styleBold = template.resource().styleContentBold().clone().fontSize(26f)
        val CONTENT_SQUARE_RATE = 12f
        val RESULT_IMAGE_LOW_RATE = 110f
        val colors = when (dto.result) {
            CancerchDto.Results.GENERAL -> Color(141, 197, 86)
            CancerchDto.Results.CONCERN -> Color(239, 167, 24)
            else -> Color(217, 52, 29)
        }

        if (dto.result == CancerchDto.Results.GENERAL) {
            stream.paragraph(375f, y-20, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(13f), "Low probability of cancer"))
        } else if (dto.result == CancerchDto.Results.CONCERN) {
            var img = template.resource().imgBackgroundCancer(dto.first.name)
            var width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 290 - width / 2, y-68, width, RESULT_IMAGE_LOW_RATE)

            img = template.resource().imgBarGray()
            width = img.width * CONTENT_SQUARE_RATE / img.height

            var height = CONTENT_SQUARE_RATE
            stream.drawImage(img, 258 - width / 2, y-98, width, height)
            stream.paragraph(258f, y-98 + height + 10, 60f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(10f), "About 1x"))

            img = template.resource().imgBarMiddle()
            height = CONTENT_SQUARE_RATE * 4f
            stream.drawImage(img, 318 - width / 2, y-98, width, height)
            stream.paragraph(318f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(colors).fontSize(13f), "About 2x higher"))
            stream.paragraph(470f, y, 300f, AlignHorizontal.CENTER, TextBlock(styleRegular.color(Color(11, 11, 11)).clone().fontSize(9f), "It is unclear to predict it as a specific\ncancer, but the possibility of cancer is\npredicted to be about "), TextBlock(styleRegular.color(Color(239, 167, 24)).clone().fontSize(9f),"twice as high."))
        } else {
            var img = template.resource().imgBackgroundCancer(dto.first.name)
            var width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 290 - width / 2, y-68, width, RESULT_IMAGE_LOW_RATE)

            img = template.resource().imgBarGray()
            width = img.width * CONTENT_SQUARE_RATE / img.height

            var height = CONTENT_SQUARE_RATE
            stream.drawImage(img, 258 - width / 2, y-98, width, height)
            stream.paragraph(258f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(10f), "About 1x"))

            img = template.resource().imgBarDanger()
            height = CONTENT_SQUARE_RATE * 8f
            stream.drawImage(img, 318 - width / 2, y-98, width, height)
            stream.paragraph(318f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(colors).fontSize(11f), "About 10x higher"))
            if (dto.first.name != "기타암종") {
                img = template.resource().imgBackgroundCancer("기타암종")
                width = img.width * RESULT_IMAGE_LOW_RATE / img.height
                stream.drawImage(img, 468 - width / 2, y-68, width, RESULT_IMAGE_LOW_RATE)

                img = template.resource().imgBarGray()
                width = img.width * CONTENT_SQUARE_RATE / img.height

                height = CONTENT_SQUARE_RATE
                stream.drawImage(img, 438 - width / 2, y-98, width, height)
                stream.paragraph(438f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(10f), "About 1x"))

                img = template.resource().imgBarDanger()
                height = CONTENT_SQUARE_RATE * 5f
                stream.drawImage(img, 498 - width / 2, y-98, width, height)
                stream.paragraph(498f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(colors).fontSize(11f), "About 5x higher"))
            } else {
                stream.paragraph(470f, y+10, 300f, AlignHorizontal.CENTER, TextBlock(styleRegular.color(Color(11, 11, 11)).clone().fontSize(9f), "Possibility of cancer other than\n6 types of cancer is predicted to be\nabout "), TextBlock(styleRegular.clone().color(Color(217, 52, 29)).fontSize(9f),"10 times higher"))
                stream.paragraph(
                    470f, y-40, 300f, AlignHorizontal.CENTER, TextBlock(
                        styleRegular.clone().fontSize(7f), "*6 cancer types: Lung, Colon, Liver, Pancreatobiliary,\nEsophageal, and Ovarian cancer."
                    )
                )
            }
        }
        return stream
    }
}
