package com.greencross.lims.report.ON204.enus.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.DNACXDto
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import java.awt.Color

class SectionIntro(private val y: Float = 708f): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACXTemplate<DNACXResource>?,
        dto: DNACXDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template!!.resource().styleContentBold().clone().color(Color(67,72,142)).fontSize(15f), "AI-powered Blood Test for CANCER Screening"))

        stream.paragraph(35f, y-38f, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().fontSize(12f),
            "ai-CANCERCH detects circulating cancer DNA signals by analyzing the pattern of circulating cell-free\n" +
                "DNA fragements in the blood using an artificial intelligence algorithm. It analyzes DNA patterns to\n" +
                "predict the likelihood of the 6 major types of cancers."))

        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y-80f).lineTo(560f,y-80f).fill()
        stream.restoreGraphicsState()
        return stream
    }
}
