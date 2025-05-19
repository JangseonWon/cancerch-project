package com.greencross.lims.report.ON206.enus.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import java.awt.Color

class SectionInterpretation(private val y: Float = 363f) : Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: DNACTTemplate<DNACTResource>,
        dto: DNACTDto
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().clone().color(Color(0,80,109)).fontSize(15f), "Interpretation"))
        stream.rect(35f, y-24f, 525f, 240f).setStrokingColor(Color(242,242,242)).setNonStrokingColor(Color(242,242,242)).fillAndStroke()

        stream.paragraph(55f, y-48f,505f, AlignHorizontal.LEFT,
            TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(14f),
                template.resource().lblInterpretatioon(dto)))
        stream.restoreGraphicsState()
        return stream
    }
}
