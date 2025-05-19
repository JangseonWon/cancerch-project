package com.greencross.lims.report.ON206.jajp.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import java.awt.Color

class SectionIntro(private val y: Float = 708f): Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: DNACTTemplate<DNACTResource>,
        dto: DNACTDto
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().clone().color(
            Color(0,80,109)
        ).fontSize(15f), "AI-powered Blood Test for CANCER Monitoring\n")
        )

        stream.paragraph(35f, y-38f, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().fontSize(12f),
            "DNA CTは、人工知能アルゴリズムを利用して血中を循環する遊離DNA（circulating cell-free DNA;\n" +
                    "cfDNA）断片のパターンを分析し、がんに関連するDNAシグナルを検出する技術です。")
        )

        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y-73f).lineTo(560f,y-73f).fill()
        stream.restoreGraphicsState()
        return stream
    }
}
