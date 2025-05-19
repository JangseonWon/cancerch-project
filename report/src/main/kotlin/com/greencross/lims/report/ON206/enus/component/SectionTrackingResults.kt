package com.greencross.lims.report.ON206.enus.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.AlignVertical
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import java.awt.Color

class SectionTrackingResults(private val y: Float = 669f) : Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: DNACTTemplate<DNACTResource>,
        dto: DNACTDto
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()
        stream.paragraph(35f, y-10f, 600f, AlignHorizontal.LEFT,
            TextBlock(template.resource().styleContentBold().clone().color(Color(0,80,109)).fontSize(15f), "DNA CT Test Tracking Results "),
            TextBlock(template.resource().styleContentRegualar().clone().color(Color(114,113,113)).fontSize(9f), "*It shows the baseline result and 4 most recent results starting from the last test."))

        val img = template.resource().imgTrackingResultsTable()
        val width = img.width * TRACKING_TABLE / img.height
        stream.drawImage(img, 298f - width / 2, y - TRACKING_TABLE - 20f, width, TRACKING_TABLE)

        val headerFont = template.resource().styleContentBold().clone().color(Color(255,255,255)).fontSize(11f)
        stream.paragraph( 65f, y-36f, 150f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(headerFont, "f/u No."))
        stream.paragraph(127f, y-36f, 100f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(headerFont, "Date"))
        stream.paragraph(212f, y-36f, 200f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(headerFont, "cfDNA\nConcentration"))
        stream.paragraph(312f, y-36f, 200f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(headerFont, "Genomic Instability\n(i-score)"))
        stream.paragraph(460f, y-28f, 200f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(headerFont, "Prob.of Tumor Signal"))
        stream.paragraph(415f, y-44f, 200f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(headerFont, "COV Score"))
        stream.paragraph(512f, y-44f, 200f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(headerFont, "FEMS Score"))

        dto.result.paragraphFormatted(stream, template)
        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y - TRACKING_TABLE - 20f).lineTo(560f,y - TRACKING_TABLE - 20f).fill()
        stream.restoreGraphicsState()
        return stream
    }

    private fun List<DNACTDto.SummaryOfResult>.paragraphFormatted(stream: PDPageContentStreamPageAccessible, template: DNACTTemplate<DNACTResource>, maxLines: Int = 5) {
        fun ordinal(n: Int) = when (n) {
            1 -> "1st"
            2 -> "2nd"
            3 -> "3rd"
            else -> "${n}th"
        }
        val font = template.resource().styleContentBold().clone().color(Color(0,0,0)).fontSize(11f)
        if(this.isEmpty()) {
            throw Exception("결과 데이터가 없습니다. 추적검사 결과를 생성할 수 없습니다.")
        }
        val baseline = this.last()
        val others = if (size > 1) dropLast(1) else emptyList()
        val count = others.size
        var repeatY = y-69f
        others.forEachIndexed { idx, item ->
            val label = ordinal(count-idx)
            stream.paragraph( 65f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font.color(Color(0,0,0)), label))
            stream.paragraph(127f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font, item.date.toString()))
            stream.paragraph(212f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font.color(if(item.cfDNAConcentrationCutOff<= item.cfDNAConcentration) Color(186,26,50) else Color(0,0,0)), item.cfDNAConcentration.toString()+" ng/ml"))
            stream.paragraph(312f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font.color(if(item.genomicInstabilityCutOff<= item.genomicInstability) Color(186,26,50) else Color(0,0,0)), item.genomicInstability.toString()))
            stream.paragraph(415f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font.color(if(item.covScoreCutOff<= item.covScore) Color(186,26,50) else Color(0,0,0)), item.covScore.toString()))
            stream.paragraph(512f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font.color(if(item.femsScoreCutOff<= item.femsScore) Color(186,26,50) else Color(0,0,0)), item.femsScore.toString()))

            repeatY -= 26f
        }

        stream.paragraph( 65f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font.color(Color(0,0,0)), "baseline"))
        stream.paragraph(127f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font, baseline.date.toString()))
        stream.paragraph(212f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font.color(if(baseline.cfDNAConcentrationCutOff<= baseline.cfDNAConcentration) Color(186,26,50) else Color(0,0,0)), baseline.cfDNAConcentration.toString()+" ng/ml"))
        stream.paragraph(312f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font.color(if(baseline.genomicInstabilityCutOff<= baseline.genomicInstability) Color(186,26,50) else Color(0,0,0)), baseline.genomicInstability.toString()))
        stream.paragraph(415f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font.color(if(baseline.covScoreCutOff<= baseline.covScore) Color(186,26,50) else Color(0,0,0)), baseline.covScore.toString()))
        stream.paragraph(512f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font.color(if(baseline.femsScoreCutOff<= baseline.femsScore) Color(186,26,50) else Color(0,0,0)), baseline.femsScore.toString()))

        repeatY -= 26f
        val printed = count + 1
        repeat(maxLines - printed.coerceAtMost(maxLines)) {
            stream.paragraph( 65f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font.color(Color(0,0,0)), "-"))
            stream.paragraph(127f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font, "-"))
            stream.paragraph(212f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font, "-"))
            stream.paragraph(312f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font, "-"))
            stream.paragraph(415f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font, "-"))
            stream.paragraph(512f, repeatY, 100f, AlignHorizontal.CENTER, TextBlock(font, "-"))
            repeatY -= 26f
        }
    }

    companion object {
        private const val TRACKING_TABLE = 169f
    }
}
