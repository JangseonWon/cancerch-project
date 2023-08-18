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
        var style = template.resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(14f)
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(297f, y+10, 300f, AlignHorizontal.CENTER, TextBlock(style, template.lblIntroHeader()))
        //endregion

        //region □ Intro Content
        img = template.resource().imgIntroContent()
        width = img.width * INTRO_CONTENT_RATE / img.height
        style = template.resource().styleContentRegualar().clone().fontSize(9f)
        stream.drawImage(img, 298f-width/2, y-CONTENT_TITLE_RATE-29, width, INTRO_CONTENT_RATE)
        stream.paragraph(51f, y-32, 500f, AlignHorizontal.LEFT, TextBlock(style, template.lblIntroContent()))
        //endregion

        stream.restoreGraphicsState()
        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val INTRO_CONTENT_RATE = 43f
    }
}