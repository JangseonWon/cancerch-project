package com.greencross.lims.report.avoid

import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter

class SectionCancerTypeDanger(private val y: Float = 390f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ Cancer's Danger content & images
        var img = template!!.resource().imgCancerTypeContent()
        var width = img.width * DANGER_CONTENT_RATE / img.height
        stream.drawImage(img, 305f - width/2, y- DANGER_CONTENT_RATE, width, DANGER_CONTENT_RATE)




        //endregion


        stream.restoreGraphicsState()
        return stream
    }
    companion object {
        private const val DANGER_CONTENT_RATE = 310f
    }
}