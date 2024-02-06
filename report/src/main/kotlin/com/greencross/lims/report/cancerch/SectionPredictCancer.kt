package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.TextStyle
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
        stream!!.saveGraphicsState()
        stream.drawImage(img, 298f - width / 2, y - DOUBT_SQUARE_RATE+10, width, 131f)
        template.lblDoubtContentTitle(stream, y, width, DOUBT_SQUARE_RATE)
        //endregion

        //region □ Doubt Content's Text
        template.lblDoubtContentLarge(stream, y, width, DOUBT_SQUARE_RATE, dto!!)
        template.lblDoubtContentSmall(stream, y, width, DOUBT_SQUARE_RATE, dto)

        if (dto.result == CancerchDto.Results.RISK) {
            img = template.resource().imgDoubtCancer(dto.first.name)
            width = img.width * DOUBT_CANCER_RATE / img.height
            stream.drawImage(
                img, 105f - width / 2f, y - DOUBT_CANCER_RATE - 42, width,
                DOUBT_CANCER_RATE
            )
        }
        stream.restoreGraphicsState()
        return stream
    }

    companion object {
        private const val DOUBT_SQUARE_RATE = 141f
        private const val DOUBT_CANCER_RATE = 76f
    }
}