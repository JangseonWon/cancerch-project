package com.greencross.lims.report.ON204.jajp.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.DNACXDto
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import java.awt.Color

class SectionTestResult(private val y: Float = 618f): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACXTemplate<DNACXResource>?,
        dto: DNACXDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template!!.resource().styleContentBold().clone().color(Color(67,72,142)).fontSize(15f), "総合結果"))

        val (color, comment) = when(dto.risk) {
            DNACXDto.Risk.LOW      -> Pair(Color(113, 191,68), "ほとんど疑いなし")
            DNACXDto.Risk.MILD     -> Pair(Color(250, 166,26), "低リスク")
            DNACXDto.Risk.MODERATE -> Pair(Color(243, 112,66), "中リスク")
            DNACXDto.Risk.HIGH     -> Pair(Color(186,  26,50), "高リスク")
        }

        stream.roundRect(120f, y-60f, 350f, 50f, 25f).setLineWidth(3f).setStrokingColor(color).stroke()
        stream.paragraph(300f, y-47f, 300f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentBold().clone().color(color).fontSize(38f), comment))

        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y-75f).lineTo(560f,y-75f).stroke()
        stream.restoreGraphicsState()
        return stream
    }
}
