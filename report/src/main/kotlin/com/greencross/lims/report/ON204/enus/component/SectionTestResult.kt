package com.greencross.lims.report.ON204.enus.component

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
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template!!.resource().styleContentBold().clone().color(Color(67,72,142)).fontSize(15f), "Test Result"))

        val (color, comment) = when(dto.risk) {
            DNACXDto.Risk.LOW      -> Pair(Color(113, 191,68), "Low Risk")
            DNACXDto.Risk.MILD     -> Pair(Color(250, 166,26), "Mild Risk")
            DNACXDto.Risk.MODERATE -> Pair(Color(243, 112,66), "Moderate Risk")
            DNACXDto.Risk.HIGH     -> Pair(Color(186,  26,50), "High Risk")
            else -> throw Exception("SectionTestResult에서 비정상적인 RISK가 입력됐습니다.")
        }

        stream.roundRect(120f, y-60f, 350f, 50f, 25f).setLineWidth(3f).setStrokingColor(color).stroke()
        stream.paragraph(300f, y-47f, 300f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentBold().clone().color(color).fontSize(38f), comment))

        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y-75f).lineTo(560f,y-75f).stroke()
        stream.restoreGraphicsState()
        return stream
    }
}
