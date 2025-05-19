package com.greencross.lims.report.ON204.jajp.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.DNACXDto
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import java.awt.Color

class SectionTestResultSmall(private val y: Float = 708f): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACXTemplate<DNACXResource>?,
        dto: DNACXDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template!!.resource().styleContentBold().clone().color(Color(67,72,142)).fontSize(15f), "総合結果"))
        val (color, comment, roundRectWidth, paragraphSx) = when(dto.risk) {
            DNACXDto.Risk.LOW      -> Quad(Color(113, 191,68), "ほとんど疑いなし", 120f, 180f)
            DNACXDto.Risk.MILD     -> Quad(Color(250, 166,26), "低リスク", 80f, 160f)
            DNACXDto.Risk.MODERATE -> Quad(Color(243, 112,66), "中リスク", 80f, 160f)
            DNACXDto.Risk.HIGH     -> Quad(Color(186,  26,50), "高リスク", 80f, 160f)
        }
        stream.roundRect(120f, y-17f, roundRectWidth, 17f, 8.5f).setLineWidth(3f).setStrokingColor(color).setNonStrokingColor(color).fillAndStroke()
        stream.paragraph(paragraphSx, y-13f, 300f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentBold().clone().color(Color(255,255,255)).fontSize(14f), comment))

        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y-29f).lineTo(560f,y-29f).fill()
        stream.restoreGraphicsState()
        return stream
    }
    data class Quad<A, B, C, D> (
        val first: A,
        val second: B,
        val third: C,
        val fourth: D
    )
}
