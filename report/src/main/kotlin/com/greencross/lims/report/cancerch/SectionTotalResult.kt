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
        stream.drawImage(img, 297f - width / 2, y, width, CONTENT_TITLE_RATE)
        img = template.resource().imgTotalResultContent()
        width = img.width * RESULT_CONTENT_RATE / img.height
        stream.drawImage(img, 298f - width / 2, y - CONTENT_TITLE_RATE - 77, 344f, RESULT_CONTENT_RATE)

        template.lblOverviewTitle(stream, y)

        img = template.resource().imgDoubtSquare()
        width = img.width * DOUBT_SQUARE_RATE / img.height
        stream.drawImage(img, 475f - width/2, y- DOUBT_SQUARE_RATE-12, width, DOUBT_SQUARE_RATE)
        template.lblDoubtSquareTitle(stream, y, width, DOUBT_SQUARE_RATE)
        template.lblDoubtSquareContent(stream, y, width, DOUBT_SQUARE_RATE, dto!!)

        template.lblOverviewResultImg(stream, y, CONTENT_TITLE_RATE, dto)
        template.lblOverviewResult(stream, y, width,  CONTENT_TITLE_RATE, dto)
        //endregion

        return stream
    }

    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val RESULT_CONTENT_RATE = 92f
        private const val DOUBT_SQUARE_RATE = 93f
    }
}