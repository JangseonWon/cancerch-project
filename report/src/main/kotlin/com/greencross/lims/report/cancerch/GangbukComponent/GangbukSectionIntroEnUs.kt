package com.greencross.lims.report.cancerch.GangbukComponent

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.cancerch.CancerchResource
import com.greencross.lims.report.cancerch.CancerchTemplate
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class GangbukSectionIntroEnUs(private val y: Float = 685f): Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(297f, y+10, 300f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentSpecial().clone().color(Color(255,255,255)).fontSize(14f), "AI-powered Blood Test for Cancer Screening"))

        img = template.resource().imgIntroContent()
        width = img.width * INTRO_CONTENT_RATE / img.height
        stream.drawImage(img, 298f-width/2, y- CONTENT_TITLE_RATE -29, width, INTRO_CONTENT_RATE)
        stream.paragraph(48f, y-29, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().fontSize(7f),
            "ai-CANCERCH is the multi cancer early detection(MCED) test powered by artificial intelligence(AI) that has trained distinctive DNA patterns from approximately\n" +
                "5,000 cancer patients and healthy individuals. It analyzes DNA patterns to predict the likelihood of the 6 major types of cancers. The test results indicate the\n" +
                "potential presence of circulating tumor DNA and require further diagnostic confirmations."))

        stream.restoreGraphicsState()
        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val INTRO_CONTENT_RATE = 43f
    }
}
