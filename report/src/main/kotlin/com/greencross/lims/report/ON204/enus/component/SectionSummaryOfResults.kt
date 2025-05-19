package com.greencross.lims.report.ON204.enus.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.DNACXDto
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import java.awt.Color

class SectionSummaryOfResults(private val y: Float = 533f): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACXTemplate<DNACXResource>?,
        dto: DNACXDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template!!.resource().styleContentBold().clone().color(Color(67,72,142)).fontSize(15f), "Summary of Result"))

        val img = template.resource().imgSummaryOfResults()
        val width = img.width * TABLE_RATE / img.height
        stream.drawImage(img, 298f - width / 2, y - TABLE_RATE - 30, width, TABLE_RATE)

        val tableHeaderFont = template.resource().styleContentBold().clone().color(Color(255,255,255)).fontSize(12f)

        stream.paragraph(193f, y-44f, 200f, AlignHorizontal.CENTER, TextBlock(tableHeaderFont, "Prob. of Tumor Signal"))
        stream.paragraph( 87f, y-65f, 200f, AlignHorizontal.CENTER, TextBlock(tableHeaderFont, "Signal Score"))
        stream.paragraph(192f, y-65f, 200f, AlignHorizontal.CENTER, TextBlock(tableHeaderFont, "COV Score"))
        stream.paragraph(298f, y-65f, 200f, AlignHorizontal.CENTER, TextBlock(tableHeaderFont, "FEMS Score"))
        stream.paragraph(403f, y-47f, 200f, AlignHorizontal.CENTER, TextBlock(tableHeaderFont, "cfDNA\nConcentration"))
        stream.paragraph(508f, y-47f, 200f, AlignHorizontal.CENTER, TextBlock(tableHeaderFont, "Genomic Instability\n(i-score)"))

        val tableValueFont = template.resource().styleContentRegualar().clone().fontSize(14f)
        stream.paragraph( 87f, y-90f, 200f, AlignHorizontal.CENTER, TextBlock(tableValueFont.color(setColorByCutOff(dto.result.signalScore95CutOff, dto.result.signalScore)), dto.result.signalScore.toString()))
        stream.paragraph(192f, y-90f, 200f, AlignHorizontal.CENTER, TextBlock(tableValueFont.color(setColorByCutOff(dto.result.covScoreCutOff, dto.result.covScore)), dto.result.covScore.toString()))
        stream.paragraph(298f, y-90f, 200f, AlignHorizontal.CENTER, TextBlock(tableValueFont.color(setColorByCutOff(dto.result.femsScoreCutOff, dto.result.femsScore)), dto.result.femsScore.toString()))
        stream.paragraph(403f, y-90f, 200f, AlignHorizontal.CENTER, TextBlock(tableValueFont.color(setColorByCutOff(dto.result.cfDNAConcentrationCutOff, dto.result.cfDNAConcentration)), dto.result.cfDNAConcentration.toString()+" ng/ml"))
        stream.paragraph(508f, y-90f, 200f, AlignHorizontal.CENTER, TextBlock(tableValueFont.color(setColorByCutOff(dto.result.genomicInstabilityCutOff, dto.result.genomicInstability)), dto.result.genomicInstability.toString()))

        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y-125f).lineTo(560f,y-125f).fill()
        stream.restoreGraphicsState()
        return stream
    }
    private fun setColorByCutOff(cutOff: Double, value: Double) : Color {
        return when {
            cutOff <= value -> Color(186,26,50)
            else -> Color(0,0,0)
        }
    }

    companion object {
        private const val TABLE_RATE = 71f
    }
}
