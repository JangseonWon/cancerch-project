package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionIntro(private val y: Float = 685f): Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ Intro Title
        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TITLE_RATE)
        template.lblIntroHeader(stream, y)
        //endregion

        //region □ Intro Content
        img = template.resource().imgIntroContent()
        width = img.width * INTRO_CONTENT_RATE / img.height
        stream.drawImage(img, 298f-width/2, y-CONTENT_TITLE_RATE-29, width, INTRO_CONTENT_RATE)
        template.lblIntroContent(stream, y)
        //endregion

        stream.restoreGraphicsState()
        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val INTRO_CONTENT_RATE = 43f
    }
}