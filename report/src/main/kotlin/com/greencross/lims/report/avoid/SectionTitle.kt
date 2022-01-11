package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.TextStyle
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.io.IOException

class SectionTitle @JvmOverloads constructor(private val y: Float = 773f) :
    Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    @Throws(IOException::class)
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: AvoidTemplate<AvoidResource>,
        dto: AvoidDto
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()
        val resource = template.resource()
        val styleTitle =
            TextStyle().color(resource.colorText()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(20f)
                .paragraph(false)
        stream.paragraph(
            297.5f,
            y, 500f, AlignHorizontal.CENTER, TextBlock(styleTitle, template.testInfo().name())
        )
        stream.restoreGraphicsState()
        return stream.cursorY(y - TITLE_HEIGHT)
    }

    companion object {
        private const val TITLE_HEIGHT = 20f
    }
}
