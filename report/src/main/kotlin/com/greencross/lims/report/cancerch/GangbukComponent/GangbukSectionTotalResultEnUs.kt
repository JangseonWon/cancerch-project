package com.greencross.lims.report.cancerch.GangbukComponent

import com.gcgenome.lims.report.TextBlock
import com.greencross.lims.report.builder.Util_EnUS
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.cancerch.CancerchResource
import com.greencross.lims.report.cancerch.CancerchTemplate
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.awt.Color

class GangbukSectionTotalResultEnUs(private val y: Float = 580f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
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
        stream.drawImage(img, 298f - width / 2, y - CONTENT_TITLE_RATE - 77, width, RESULT_CONTENT_RATE)

        stream.paragraph(297f, y + 11, 300f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "Test Results"))

        val RESULT_IMAGE_RATE = 62f

        when(dto!!.result) {
            CancerchDto.Results.GENERAL -> {
                val img = template.resource().imgTotalResultLowRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 365f - width / 2, y - CONTENT_TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
            CancerchDto.Results.CONCERN -> {
                val img = template.resource().imgTotalResultMiddleRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 365f - width / 2, y - CONTENT_TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
            CancerchDto.Results.RISK -> {
                val img = template.resource().imgTotalResultHighRisk()
                val width = img.width * RESULT_IMAGE_RATE / img.height
                stream.drawImage(img, 365f - width / 2, y - CONTENT_TITLE_RATE - 64, width, RESULT_IMAGE_RATE)
            }
        }
        when(dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(
                257f,
                y - CONTENT_TITLE_RATE - 36,
                200f,
                AlignHorizontal.RIGHT, TextBlock(template.resource().styleContentSpecial().clone().fontSize(20f).color(Color(141, 197, 86)), Util_EnUS.lblResultToWord(dto.result)))
            CancerchDto.Results.CONCERN -> stream.paragraph(
                282f,
                y - CONTENT_TITLE_RATE - 36,
                200f,
                AlignHorizontal.RIGHT, TextBlock(template.resource().styleContentSpecial().clone().fontSize(20f).color(Color(239, 167, 24)), Util_EnUS.lblResultToWord(dto.result)))
            CancerchDto.Results.RISK -> stream.paragraph(
                257f,
                y - CONTENT_TITLE_RATE - 36,
                200f,
                AlignHorizontal.RIGHT, TextBlock(template.resource().styleContentSpecial().clone().fontSize(20f).color(Color(217, 52, 29)), Util_EnUS.lblResultToWord(dto.result)))
        }
        //endregion
        stream.restoreGraphicsState()
        return stream
    }

    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val RESULT_CONTENT_RATE = 92f
    }
}
