package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.builder.Util.Companion.lblPatientSir
import com.greencross.lims.report.builder.Util.Companion.lblPatientInfo
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionAnalysis(private val y: Float = 200f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.line(35f, y+113, 560f, y+113).setStrokingColor(Color(67, 72, 142)).setLineWidth(1f).stroke()
        stream.line(35f, y+67, 560f, y+67).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
        stream.line(35f, y-98, 560f, y-98).setStrokingColor(Color(67, 72, 142)).setLineWidth(1f).stroke()
        stream.line(195f, y+113, 195f, y-98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()


        var styleRegular = template!!.resource().styleContentRegualar().clone().fontSize(6f)
        stream.paragraph(113f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "암종"))

        var styleBold = template.resource().styleContentBold().clone().fontSize(26f)
        if(dto!!.result == CancerchDto.Results.RISK) {
            if (dto.first.name == "기타암종") {
                stream.line(375f,  y+67, 375f, y-98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
                stream.paragraph(365f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(13f),"암종 예측 불가"))
            } else {
                stream.line(375f,  y+113, 375f, y-98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
                stream.paragraph(290f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.fontSize(13f), "의심 암종 : ${dto.first.name}"))
                stream.paragraph(468f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.fontSize(13f), "기타 암"))
            }
        } else {
            stream.line(375f, y + 67, 375f, y - 98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
            val results = when (dto!!.result) {
                CancerchDto.Results.GENERAL -> "해당없음"
                else -> "해당없음 : 추적관찰 권장"
            }
            stream.paragraph(375f, y + 85, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(13f), results))
        }
        styleRegular = template.resource().styleContentRegualar().clone().fontSize(6f)
        if (dto.result != CancerchDto.Results.GENERAL) {
            stream.rect(45f, y-27f, 138f, 53f).setNonStrokingColor(Color(242,242,247)).fill()
            if(dto.result == CancerchDto.Results.RISK) stream.rect(56f, y-40.5f, 10f, 10f).setNonStrokingColor(Color(217,  52, 29)).fill()
            else stream.rect(56f, y-40.5f, 10f, 10f).setNonStrokingColor(Color(239, 167, 24)).fill()
            stream.rect(56f, y-59f, 10f, 10f).setNonStrokingColor(Color(108,109,112)).fill()
            stream.paragraph(71f, y - 49, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(10.5f), lblPatientSir(dto.patientName!!)))
            stream.paragraph(71f, y - 67, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(10.5f), lblPatientInfo(dto.age!!, dto.sex!!)))
            stream.paragraph(113f, y, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "위험도 비교"))
        } else {
            stream.paragraph(113f, y-20, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "위험도 비교"))
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
            stream.paragraph(290f, y-20, 50f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(67, 72, 142)).clone().fontSize(11f), "해당없음"))
            stream.paragraph(470f, y-20, 200f, AlignHorizontal.CENTER, TextBlock(styleBold.color(Color(128, 128, 128)).clone().fontSize(11f), "암의 존재가능성이 낮음"))
        } else if (dto.result == CancerchDto.Results.CONCERN) {
            var img = template.resource().imgBackgroundCancer(dto.first.name)
            var width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 290 - width / 2, y-68, width, RESULT_IMAGE_LOW_RATE)

            img = template.resource().imgBarGray()
            width = img.width * CONTENT_SQUARE_RATE / img.height

            var height = CONTENT_SQUARE_RATE
            stream.drawImage(img, 258 - width / 2, y-98, width, height)
            stream.paragraph(258f, y-98 + height + 10, 60f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "약 1배"))

            img = template.resource().imgBarMiddle()
            height = CONTENT_SQUARE_RATE * 4f
            stream.drawImage(img, 318 - width / 2, y-98, width, height)
            stream.paragraph(318f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(colors).fontSize(13f), "약 2배 이상"))
            stream.paragraph(470f, y, 300f, AlignHorizontal.CENTER, TextBlock(styleRegular.color(Color(11, 11, 11)).clone().fontSize(11f), "특정 암으로 예측하기에는\n" +
                    "불분명하나 암 존재 가능성이\n" +
                    "약 2배 이상 높을 것으로 예측됨"))
        } else {
            var img = template.resource().imgBackgroundCancer(dto.first.name)
            var width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(img, 290 - width / 2, y-68, width, RESULT_IMAGE_LOW_RATE)

            img = template.resource().imgBarGray()
            width = img.width * CONTENT_SQUARE_RATE / img.height

            var height = CONTENT_SQUARE_RATE
            stream.drawImage(img, 258 - width / 2, y-98, width, height)
            stream.paragraph(258f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "약 1배"))

            img = template.resource().imgBarDanger()
            height = CONTENT_SQUARE_RATE * 8f
            stream.drawImage(img, 318 - width / 2, y-98, width, height)
            stream.paragraph(318f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(colors).fontSize(13f), "약 10배 이상"))
            if (dto.first.name != "기타암종") {
                img = template.resource().imgBackgroundCancer("기타암종")
                width = img.width * RESULT_IMAGE_LOW_RATE / img.height
                stream.drawImage(img, 468 - width / 2, y-68, width, RESULT_IMAGE_LOW_RATE)

                img = template.resource().imgBarGray()
                width = img.width * CONTENT_SQUARE_RATE / img.height

                height = CONTENT_SQUARE_RATE
                stream.drawImage(img, 438 - width / 2, y-98, width, height)
                stream.paragraph(438f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(0, 0, 0)).fontSize(13f), "약 1배"))

                img = template.resource().imgBarDanger()
                height = CONTENT_SQUARE_RATE * 5f
                stream.drawImage(img, 498 - width / 2, y-98, width, height)
                stream.paragraph(498f, y-98 + height + 10, 120f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(colors).fontSize(13f), "약 5배 이상"))
            } else {
                stream.paragraph(470f, y, 300f, AlignHorizontal.CENTER, TextBlock(styleRegular.color(Color(11, 11, 11)).clone().fontSize(11f), "6종 암 또는 기타암의\n존재 가능성이 약 10배 이상\n높을 것으로 예측됨"))
            }
        }
        return stream
    }
}
