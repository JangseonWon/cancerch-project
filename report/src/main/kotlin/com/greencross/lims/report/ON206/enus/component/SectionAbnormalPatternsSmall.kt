package com.greencross.lims.report.ON206.enus.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import java.awt.Color

class SectionAbnormalPatternsSmall(private val y: Float = 708f): Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: DNACTTemplate<DNACTResource>,
        dto: DNACTDto
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().clone().color(Color(0,80,109)).fontSize(15f), "Abnormal Patterns"))

        val (color, comment) = when(dto.risk) {
            DNACTDto.Risk.NOT_DETECTED  -> Pair(Color(113, 191,68), "NOT Detected")
            DNACTDto.Risk.WEAK          -> Pair(Color(250, 166,26), "Detected (Weak)")
            DNACTDto.Risk.MODERATE      -> Pair(Color(243, 112,66), "Detected (Moderate)")
            DNACTDto.Risk.STRONG        -> Pair(Color(186,  26,50), "Detected (Strong)")
        }
        stream.roundRect(170f, y-17f, 140f, 17f, 8.5f).setLineWidth(3f).setStrokingColor(color).setNonStrokingColor(color).fillAndStroke()
        stream.paragraph(240f, y-13f, 300f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentBold().clone().color(Color(255,255,255)).fontSize(14f), comment))

        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y-29f).lineTo(560f,y-29f).fill()
        stream.restoreGraphicsState()
        return stream
    }
}
