package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionTotalResult(private val y: Float = 580f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ TotalResult Title, Content Img
        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        var style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
        stream.drawImage(img, 297f - width / 2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(297f, y + 11, 300f, AlignHorizontal.CENTER, TextBlock(style, template.lblOverviewTitle()))
        img = template.resource().imgTotalResultContent()
        width = img.width * RESULT_CONTENT_RATE / img.height
        stream.drawImage(
            img, 298f - width / 2, y - CONTENT_TITLE_RATE - 77, 344f,
            RESULT_CONTENT_RATE
        )

        img = template.resource().imgDoubtSquare()
        width = img.width * DOUBT_SQUARE_RATE / img.height
        style = template.resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(14f)
        stream.drawImage(img, 475f - width/2, y- DOUBT_SQUARE_RATE-12, width, DOUBT_SQUARE_RATE)
        stream.paragraph(562f - width/2,y - DOUBT_SQUARE_RATE + 53, 300f, AlignHorizontal.CENTER, TextBlock(style, template.lblDoubtSquareTitle()))

        val colors = when(dto!!.result){
            CancerchDto.Results.GENERAL     -> Color(141, 197, 86)
            CancerchDto.Results.CONCERN     -> Color(239, 167, 24)
            else                            -> Color(217,  52, 29)
        }
        style = template.resource().styleContentSpecial().clone().color(colors).fontSize(18f)
        stream.paragraph(562f - width/2,y - DOUBT_SQUARE_RATE+12, 300f, AlignHorizontal.CENTER,
            TextBlock(style, template.lblDoubtSquareContent(dto.result)))

        if (dto.result == CancerchDto.Results.GENERAL) {
            img = template.resource().imgTotalResultLowRisk()
            width = img.width * RESULT_IMAGE_LOW_RATE / img.height
            stream.drawImage(
                img, 272f - width / 2, y - CONTENT_TITLE_RATE - 64, width,
                RESULT_IMAGE_LOW_RATE
            )
            stream.paragraph(
                177f,
                y - CONTENT_TITLE_RATE - 41,
                200f,
                AlignHorizontal.RIGHT,
                TextBlock(style.clone().fontSize(27f).color(Color(141, 197, 86)), template.lblResultToWord(dto.result))
            )
        } else if (dto.result == CancerchDto.Results.CONCERN) {
            img = template.resource().imgTotalResultMiddleRisk()
            width = img.width * RESULT_IMAGE_HIGH_RATE / img.height
            stream.drawImage(
                img, 272f - width / 2, y - CONTENT_TITLE_RATE - 64, width,
                RESULT_IMAGE_HIGH_RATE
            )
            stream.paragraph(
                177f,
                y - CONTENT_TITLE_RATE - 41,
                200f,
                AlignHorizontal.RIGHT,
                TextBlock(style.clone().fontSize(27f).color(Color(239, 167, 24)), template.lblResultToWord(dto.result))
            )
        } else {
            img = template.resource().imgTotalResultHighRisk()
            width = img.width * RESULT_IMAGE_HIGH_RATE / img.height
            stream.drawImage(
                img, 272f - width / 2, y - CONTENT_TITLE_RATE - 64, width,
                RESULT_IMAGE_HIGH_RATE
            )
            stream.paragraph(
                177f,
                y - CONTENT_TITLE_RATE - 41,
                200f,
                AlignHorizontal.RIGHT,
                TextBlock(style.clone().fontSize(27f).color(Color(217, 52, 29)), template.lblResultToWord(dto.result))
            )
        }

        //endregion

        return stream
    }

    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val RESULT_CONTENT_RATE = 92f
        private const val RESULT_IMAGE_LOW_RATE = 64f
        private const val RESULT_IMAGE_HIGH_RATE = 64f
        private const val DOUBT_SQUARE_RATE = 93f
    }
}