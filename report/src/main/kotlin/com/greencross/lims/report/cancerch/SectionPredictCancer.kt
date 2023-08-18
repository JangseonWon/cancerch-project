package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionPredictCancer(private val y: Float = 457f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        var img = template!!.resource().imgDoubtContentBox()
        var width = 527f
        var style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
        stream!!.drawImage(img, 298f - width / 2, y - DOUBT_SQUARE_RATE+10, width, 131f)
        stream.paragraph(
            562f - width / 2, y - DOUBT_SQUARE_RATE + 121, 300f, AlignHorizontal.CENTER,
            TextBlock(style, template.lblDoubtContentTitle())
        )
        //endregion

        //region □ Doubt Content's Text
        val horizontal = when (dto!!.result) {
            CancerchDto.Results.RISK -> AlignHorizontal.LEFT
            else -> AlignHorizontal.CENTER
        }
        val x = when (dto.result) {
            CancerchDto.Results.GENERAL -> 423f - width / 2
            CancerchDto.Results.CONCERN -> 423f - width / 2
            else -> 323f - width / 2
        }
        val ys = when (dto.result) {
            CancerchDto.Results.GENERAL -> y - DOUBT_SQUARE_RATE + 75
            else -> y - DOUBT_SQUARE_RATE + 75
        }
        style = template.resource().styleContentSpecial().clone().color(Color(67, 72, 142)).fontSize(13f)
        stream.paragraph(
            x + 130, ys, 400f, horizontal,
            TextBlock(style, template.lblDoubtContentLarge(dto.result, dto.first.name))
        )
        style = template.resource().styleContentRegualar().clone().color(Color(0, 0, 0)).fontSize(10f)
        stream.paragraph(
            x + 130, ys - 27, 300f, horizontal,
            TextBlock(style, template.lblDoubtContentSmall(dto.result))
        )

        if (dto.result == CancerchDto.Results.RISK) {
            img = template.resource().imgDoubtCancer(dto.first.name)
            width = img.width * DOUBT_CANCER_RATE / img.height
            stream.drawImage(
                img, 105f - width / 2f, y - DOUBT_CANCER_RATE - 30, width,
                DOUBT_CANCER_RATE
            )
        }
        stream.restoreGraphicsState()
        return stream
    }

    companion object {
        private const val DOUBT_SQUARE_RATE = 141f
        private const val DOUBT_CANCER_RATE = 96f
    }
}