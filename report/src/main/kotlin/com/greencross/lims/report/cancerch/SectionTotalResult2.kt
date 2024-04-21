package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.builder.Util_EnUS
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionTotalResult2(private val y: Float = 580f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ TotalResult Title, Content Img
        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 297f - width / 2, y, width, CONTENT_TITLE_RATE)
        img = template.resource().imgTotalResultContent()
        width = img.width * RESULT_CONTENT_RATE / img.height
        stream.drawImage(img, 298f - width / 2, y - CONTENT_TITLE_RATE - 77, 344f, RESULT_CONTENT_RATE)

        stream.paragraph(297f, y + 11, 300f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "Test Results"))

        img = template.resource().imgDoubtSquare()
        width = img.width * DOUBT_SQUARE_RATE / img.height
        stream.drawImage(img, 475f - width/2, y- DOUBT_SQUARE_RATE-12, width, DOUBT_SQUARE_RATE)
        stream.paragraph(562f - width/2,y - DOUBT_SQUARE_RATE + 53, 300f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(14f),"Abnormal Patterns"))
        val colors = when(dto!!.result) {
            CancerchDto.Results.GENERAL     -> Color(141, 197, 86)
            CancerchDto.Results.CONCERN     -> Color(239, 167, 24)
            else                            -> Color(217,  52, 29)
        }
        when (dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(562f - width/2,y - DOUBT_SQUARE_RATE+12, 300f, AlignHorizontal.CENTER,
                TextBlock(template.resource().styleContentSpecial().clone().color(colors).fontSize(18f), "Not detected"))
            else -> stream.paragraph(562f - width/2,y - DOUBT_SQUARE_RATE+12, 300f, AlignHorizontal.CENTER,
                TextBlock(template.resource().styleContentSpecial().clone().color(colors).fontSize(18f), "Detected"))
        }

        val RESULT_IMAGE_RATE = 62f

        when(dto.result) {
            CancerchDto.Results.GENERAL -> {
                val img = template.resource().imgTotalResultLowRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 295f - width / 2, y - CONTENT_TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
            CancerchDto.Results.CONCERN -> {
                val img = template.resource().imgTotalResultMiddleRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 295f - width / 2, y - CONTENT_TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
            CancerchDto.Results.RISK -> {
                val img = template.resource().imgTotalResultHighRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 295f - width / 2, y - CONTENT_TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
        }
        when(dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(
                187f,
                y - CONTENT_TITLE_RATE - 36,
                200f,
                AlignHorizontal.RIGHT, TextBlock(template.resource().styleContentSpecial().clone().fontSize(20f).color(Color(141, 197, 86)), Util_EnUS.lblResultToWord(dto.result)))
            CancerchDto.Results.CONCERN -> stream.paragraph(
                212f,
                y - CONTENT_TITLE_RATE - 36,
                200f,
                AlignHorizontal.RIGHT, TextBlock(template.resource().styleContentSpecial().clone().fontSize(20f).color(Color(239, 167, 24)), Util_EnUS.lblResultToWord(dto.result)))
            CancerchDto.Results.RISK -> stream.paragraph(
                187f,
                y - CONTENT_TITLE_RATE - 36,
                200f,
                AlignHorizontal.RIGHT, TextBlock(template.resource().styleContentSpecial().clone().fontSize(20f).color(Color(217, 52, 29)), Util_EnUS.lblResultToWord(dto.result)))
        }
        //endregion

        return stream
    }

    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val RESULT_CONTENT_RATE = 92f
        private const val DOUBT_SQUARE_RATE = 93f
    }
}
