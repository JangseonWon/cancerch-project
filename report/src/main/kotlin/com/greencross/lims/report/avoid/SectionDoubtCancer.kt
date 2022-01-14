package com.greencross.lims.report.avoid

import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter

class SectionDoubtCancer (private val y: Float = 488f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ Doubt Square Box, Title, Content
        var img = template!!.resource().imgDoubtSquare()
        var width = img.width * DOUBT_SQUARE_RATE / img.height
        stream.drawImage(img, 90f - width/2, y-DOUBT_SQUARE_RATE, width, DOUBT_SQUARE_RATE)

        img = template.resource().imgDoubtContentBox()
        width = img.width * DOUBT_SQUARE_RATE / img.height
        stream.drawImage(img, 357f - width/2, y-DOUBT_SQUARE_RATE, width, DOUBT_SQUARE_RATE )

        img = template.resource().imgDoubtCenterLine()
        width = img.width * DOUBT_CENTER_LINE_RATE / img.height
        stream.drawImage(img, 357f - width/2, y- DOUBT_CENTER_LINE_RATE-25, width, DOUBT_CENTER_LINE_RATE)
        //endregion

        stream.restoreGraphicsState()
        return stream
    }
    companion object {
        private const val DOUBT_SQUARE_RATE = 93f
        private const val DOUBT_CENTER_LINE_RATE = 65f
    }
}