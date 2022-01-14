package com.greencross.lims.report.kokr

import com.greencross.lims.report.Template
import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.TextStyle
import com.greencross.lims.report.builder.AbstractReportDto
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Color

class SectionPage<T : Template<*>, D : AbstractReportDto>(
    private val x: Float,
    private val y: Float,
    private val font: PDFont
) :
    Painter<T, D> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: T,
        dto: D
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()
        val ts = TextStyle().color(Color.decode("#808080")).fonts(font)
            .fontSize(8f).justify(false).paragraph(false)
        for (i in 0 until template!!.resource().doc().numberOfPages) {
            val page = template.resource().doc().getPage(i)
            val s = PDPageContentStreamPageAccessible.append(template.resource().doc(), page)
            val str = "[ " + (i + 1) + " / " + template.resource().doc().numberOfPages + " ]"
            s.paragraph(x, y + 5, 80f, AlignHorizontal.RIGHT, TextBlock(ts, str))
            s.close()
        }
        stream.restoreGraphicsState()
        return stream
    }
}