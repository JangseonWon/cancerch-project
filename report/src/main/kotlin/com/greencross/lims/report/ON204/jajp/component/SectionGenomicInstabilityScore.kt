package com.greencross.lims.report.ON204.jajp.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.AlignVertical
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON204.DNACXDto
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import java.awt.Color

class SectionGenomicInstabilityScore(private val y: Float = 295f): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACXTemplate<DNACXResource>?,
        dto: DNACXDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        var img = template!!.resource().imgLongSmallTitle()
        var width = img.width * SMALL_TITLE_RATE / img.height
        stream.drawImage(img, 115f - width / 2, y - SMALL_TITLE_RATE - 10, width, SMALL_TITLE_RATE)
        stream.paragraph(38f, y - SMALL_TITLE_RATE-5, 200f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().clone().fontSize(13f).color(Color(255,255,255)), "ゲノム不安定性(i-score)"))
        stream.paragraph(205f, y- SMALL_TITLE_RATE-5, 400f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().fontSize(11f).color(Color(114,113,113)), "ゲノム不安定性プロットは、がん患者で観察された全\n" +
                "染色体にわたる数値的変化を示しています。"))

        img = template.resource().imgGenomicInstabilityScoreBackgorund()
        width = img.width * BACKGROUND_RATE / img.height
        stream.drawImage(img, 298f - width / 2, y - BACKGROUND_RATE - 80f, width, BACKGROUND_RATE)
        //3.00 x값 = 135f / 10.00 x값 = 545f
        //0.01 당
        stream.restoreGraphicsState()
        stream.saveGraphicsState()
        stream.setLineWidth(2f).setStrokingColor(Color(186,26,50)).setNonStrokingColor(Color(186,26,50))
            .setLineDashPattern(floatArrayOf(1.7f, 2f), 1f).moveTo(137f+((dto.result.genomicInstabilityCutOff.toFloat()-3)*100*0.5857f), y - BACKGROUND_RATE - 50f).lineTo(137f+((dto.result.genomicInstabilityCutOff.toFloat()-3)*100*0.5857f), y - BACKGROUND_RATE + 42f).stroke()
        stream.restoreGraphicsState()
        stream.saveGraphicsState()
        stream.setLineWidth(1.5f).setStrokingColor(Color(67,72,143)).setNonStrokingColor(Color(67,72,143))
            .setLineDashPattern(floatArrayOf(2f, 1.7f), 3f).moveTo(137f+((dto.result.genomicInstability.toFloat()-3)*100*0.5857f), y - BACKGROUND_RATE - 50f).lineTo(137f+((dto.result.genomicInstability.toFloat()-3)*100*0.5857f),y - BACKGROUND_RATE + 42f).stroke()
        stream.circle(137f+((dto.result.genomicInstability.toFloat()-3)*100*0.5857f),y - BACKGROUND_RATE + 42f, 2.5f).fill()
        stream.restoreGraphicsState()
        stream.saveGraphicsState()
        stream.paragraph(137f+((dto.result.genomicInstability.toFloat()-3)*100*0.5857f),y - BACKGROUND_RATE + 70f, 200f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(template.resource().styleContentBold().clone().color(Color(0,0,0)).fontSize(10f), "あなたのスコア"))
        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(137f+((dto.result.genomicInstability.toFloat()-3)*100*0.5857f)-35f, y - BACKGROUND_RATE + 62f).lineTo(137f+((dto.result.genomicInstability.toFloat()-3)*100*0.5857f)+37f,y - BACKGROUND_RATE + 62f).stroke()
        stream.paragraph(137f+((dto.result.genomicInstability.toFloat()-3)*100*0.5857f),y - BACKGROUND_RATE + 53f, 200f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(template.resource().styleContentBold().clone().color(if(dto.result.genomicInstability >= dto.result.genomicInstabilityCutOff) Color(186, 26, 50) else Color(111,186,44)).fontSize(14f), "${dto.result.genomicInstability}"))
        stream.restoreGraphicsState()

        return stream
    }
    companion object {
        private const val SMALL_TITLE_RATE = 18f
        private const val BACKGROUND_RATE = 122f
    }
}
