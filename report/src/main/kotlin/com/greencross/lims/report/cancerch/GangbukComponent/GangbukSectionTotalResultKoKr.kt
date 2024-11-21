package com.greencross.lims.report.cancerch.GangbukComponent

import com.gcgenome.lims.report.TextBlock
import com.greencross.lims.report.builder.Util
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.cancerch.CancerchResource
import com.greencross.lims.report.cancerch.CancerchTemplate
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.awt.Color

class GangbukSectionTotalResultKoKr(private val y: Float = 580f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 297f - width / 2, y, width, CONTENT_TITLE_RATE)
        img = template.resource().imgTotalResultContent()
        width = img.width * RESULT_CONTENT_RATE / img.height
        stream.drawImage(img, 298f - width / 2, y - CONTENT_TITLE_RATE - 77, 344f, RESULT_CONTENT_RATE)

        stream.paragraph(297f, y + 11, 300f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "종 합 결 과"))

        img = template.resource().imgDoubtSquare()
        width = img.width * DOUBT_SQUARE_RATE / img.height
        stream.drawImage(img, 475f - width/2, y- DOUBT_SQUARE_RATE -12, width, DOUBT_SQUARE_RATE)
        stream.paragraph(562f - width/2,y - DOUBT_SQUARE_RATE + 53, 300f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(14f),"이상 패턴 검출 여부"))
        val colors = when(dto!!.result){
            CancerchDto.Results.GENERAL -> Color(141, 197, 86)
            CancerchDto.Results.CONCERN -> Color(239, 167, 24)
            else                            -> Color(217,  52, 29)
        }
        when (dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(562f - width/2,y - DOUBT_SQUARE_RATE +12, 300f, AlignHorizontal.CENTER,
                TextBlock(template.resource().styleContentSpecial().clone().color(colors).fontSize(18f), "미검출"))
            else -> stream.paragraph(562f - width/2,y - DOUBT_SQUARE_RATE +12, 300f, AlignHorizontal.CENTER,
                TextBlock(template.resource().styleContentSpecial().clone().color(colors).fontSize(18f), "검 출"))
        }
        val RESULT_IMAGE_RATE = 64f

        when(dto.result) {
            CancerchDto.Results.GENERAL -> {
                val img = template.resource().imgTotalResultLowRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 272f - width / 2, y - CONTENT_TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
            CancerchDto.Results.CONCERN -> {
                val img = template.resource().imgTotalResultMiddleRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 272f - width / 2, y - CONTENT_TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
            CancerchDto.Results.RISK -> {
                val img = template.resource().imgTotalResultHighRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 272f - width / 2, y - CONTENT_TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
        }
        stream.paragraph(
            177f,
            y - CONTENT_TITLE_RATE - 41,
            200f,
            AlignHorizontal.RIGHT,
            when(dto.result) {
                CancerchDto.Results.GENERAL ->  TextBlock(template.resource().styleContentSpecial().clone().fontSize(27f).color(Color(141, 197, 86)),   Util.lblResultToWord(dto.result))
                CancerchDto.Results.CONCERN ->  TextBlock(template.resource().styleContentSpecial().clone().fontSize(27f).color(Color(239, 167, 24)),   Util.lblResultToWord(dto.result))
                CancerchDto.Results.RISK ->     TextBlock(template.resource().styleContentSpecial().clone().fontSize(27f).color(Color(217, 52, 29)),    Util.lblResultToWord(dto.result))
            })

        stream.restoreGraphicsState()
        return stream
    }

    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val RESULT_CONTENT_RATE = 92f
        private const val DOUBT_SQUARE_RATE = 93f
    }
}
