package com.greencross.lims.report.ON206.enus.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import java.awt.Color

class SectionClinicalInformation(private val y: Float = 625f): Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: DNACTTemplate<DNACTResource>,
        dto: DNACTDto
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().clone().color(Color(0,80,109)).fontSize(15f), "Clinical Information"))
        stream.paragraph(182f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(15f), dto.result[0].cancer))

        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y-28f).lineTo(560f,y-28f).fill()
        stream.restoreGraphicsState()
        return stream
    }
}
