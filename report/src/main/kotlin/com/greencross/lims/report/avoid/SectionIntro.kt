package com.greencross.lims.report.avoid

import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter

class SectionIntro (private val y: Float = 675f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ Intro Title, Content IMG
        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_TITLE_RATE)

        img = template.resource().imgIntroContent()
        width = img.width * INTRO_CONTENT_RATE / img.height
        stream.drawImage(img, 305f-width/2, y-CONTENT_TITLE_RATE-25, width, INTRO_CONTENT_RATE)
        //endregion

        //region □ Intro Title, Content IMG

        //endregion

        stream.restoreGraphicsState()
        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 24f
        private const val INTRO_CONTENT_RATE = 43f
    }
}