package com.greencross.lims.report.avoid

import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter

class SectionTotalResult(private val y: Float = 595f) : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ TotalResult Title, Content Img
        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_TITLE_RATE)

        img = template.resource().imgTotalResultContent()
        width = img.width * RESULT_CONTENT_RATE / img.height
        stream.drawImage(img, 305f-width/2, y- CONTENT_TITLE_RATE-75, width, RESULT_CONTENT_RATE)
        //endregion

        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 24f
        private const val RESULT_CONTENT_RATE = 92f
    }
}