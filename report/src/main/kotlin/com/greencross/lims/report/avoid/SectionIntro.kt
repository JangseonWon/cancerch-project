package com.greencross.lims.report.avoid

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.awt.Color

class SectionIntro(private val y: Float = 685f) : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ Intro Title
        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        var style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(11f)
        stream.drawImage(img, 305f - width / 2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(305f, y + 7, 300f, AlignHorizontal.CENTER, TextBlock(style, template.lblIntroHeader()))
        //endregion

        //region □ Intro Content
        img = template.resource().imgIntroContent()
        width = img.width * INTRO_CONTENT_RATE / img.height
        style = template.resource().styleContentRegualar().clone().fontSize(8.5f)
        stream.drawImage(img, 305f - width / 2, y - CONTENT_TITLE_RATE - 25, width, INTRO_CONTENT_RATE)
        stream.paragraph(75f, y - 23, 500f, AlignHorizontal.LEFT, TextBlock(style, template.lblIntroContent()))
        //endregion

        stream.restoreGraphicsState()
        return stream
    }

    companion object {
        private const val CONTENT_TITLE_RATE = 21f
        private const val INTRO_CONTENT_RATE = 40f
    }
}
