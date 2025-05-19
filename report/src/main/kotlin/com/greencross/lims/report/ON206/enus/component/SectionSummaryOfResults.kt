package com.greencross.lims.report.ON206.enus.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import java.awt.Color

class SectionSummaryOfResults(private val y: Float = 512f): Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: DNACTTemplate<DNACTResource>,
        dto: DNACTDto
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().clone().color(Color(67,72,142)).fontSize(15f), "Summary of Results"))
        val img = template.resource().imgSummaryOfResultsTable()
        val width = img.width * TABLE_RATE / img.height
        stream.drawImage(img, 298f - width / 2, y - TABLE_RATE - 30, width, TABLE_RATE)

        val tableHeaderFont = template.resource().styleContentBold().clone().color(Color(255,255,255)).fontSize(12f)

        stream.paragraph(151f, y-49f, 200f, AlignHorizontal.CENTER, TextBlock(tableHeaderFont, "cfDNA\nConcentration"))
        stream.paragraph(248f, y-49f, 200f, AlignHorizontal.CENTER, TextBlock(tableHeaderFont, "Genomic Instability\n(i-score)"))
        stream.paragraph(367f, y-49f, 200f, AlignHorizontal.CENTER, TextBlock(tableHeaderFont, "Prob. of Tumor Signal\n- COV Score"))
        stream.paragraph(495f, y-49f, 200f, AlignHorizontal.CENTER, TextBlock(tableHeaderFont, "Prob. of Tumor Signal\n- FEMS Score"))

        val tableExampleFont = template.resource().styleContentRegualar().clone().color(Color(114,113,113)).fontSize(12f)
        stream.paragraph( 69f, y-98f, 200f, AlignHorizontal.CENTER, TextBlock(tableExampleFont, "Normal range"))
        stream.paragraph(151f, y-98f, 200f, AlignHorizontal.CENTER, TextBlock(tableExampleFont, "≤ ${dto.result[0].cfDNAConcentrationCutOff} ng/ml"))
        stream.paragraph(248f, y-98f, 200f, AlignHorizontal.CENTER, TextBlock(tableExampleFont, "≤ ${dto.result[0].genomicInstabilityCutOff}"))
        stream.paragraph(367f, y-98f, 200f, AlignHorizontal.CENTER, TextBlock(tableExampleFont, "≤ ${dto.result[0].covScoreCutOff}"))
        stream.paragraph(495f, y-98f, 200f, AlignHorizontal.CENTER, TextBlock(tableExampleFont, "≤ ${dto.result[0].femsScoreCutOff}"))

        val tableValueFont = template.resource().styleContentBold().clone().fontSize(14f)
        stream.paragraph( 69f, y-129f, 200f, AlignHorizontal.CENTER, TextBlock(tableValueFont.color(Color(0,0,0)), "Your result"))
        stream.paragraph(151f, y-129f, 200f, AlignHorizontal.CENTER, TextBlock(tableValueFont.color(setColorByCutOff(dto.result[0].cfDNAConcentrationCutOff, dto.result[0].cfDNAConcentration)), "≤ ${dto.result[0].cfDNAConcentration} ng/ml"))
        stream.paragraph(248f, y-129f, 200f, AlignHorizontal.CENTER, TextBlock(tableValueFont.color(setColorByCutOff(dto.result[0].genomicInstabilityCutOff, dto.result[0].genomicInstability)), "≤ ${dto.result[0].genomicInstability}"))
        stream.paragraph(367f, y-129f, 200f, AlignHorizontal.CENTER, TextBlock(tableValueFont.color(setColorByCutOff(dto.result[0].covScoreCutOff, dto.result[0].covScore)), "≤ ${dto.result[0].covScore}"))
        stream.paragraph(495f, y-129f, 200f, AlignHorizontal.CENTER, TextBlock(tableValueFont.color(setColorByCutOff(dto.result[0].femsScoreCutOff, dto.result[0].femsScore)), "≤ ${dto.result[0].femsScore}"))

        stream.restoreGraphicsState()
        return stream
    }
    private fun setColorByCutOff(cutOff: Double, value: Float) : Color {
        return when {
            cutOff <= value -> Color(186,26,50)
            else -> Color(0,0,0)
        }
    }

    companion object {
        private const val TABLE_RATE = 110f
    }
}
