package com.greencross.lims.report.ON204.jajp.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.DNACXDto
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import java.awt.Color

class SectionInterpretation(private val y: Float = 669f): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACXTemplate<DNACXResource>?,
        dto: DNACXDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template!!.resource().styleContentBold().clone().color(Color(67,72,142)).fontSize(15f), "解釈"))
        stream.rect(35f, y-24f, 525f, 240f).setStrokingColor(Color(242,242,242)).setNonStrokingColor(Color(242,242,242)).fillAndStroke()

        stream.paragraph(55f, y-40f,505f, AlignHorizontal.LEFT,
            TextBlock(template.resource().styleContentBold().clone().color(Color(0,0,0)).fontSize(13f), template.resource().lblInterpretationBold(dto)+"\n\n"),
            TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(13f),
                template.resource().lblInterpretationRegular(dto)))

        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y-280f).lineTo(560f,y-280f).fill()
        stream.restoreGraphicsState()
        return stream
    }
}
