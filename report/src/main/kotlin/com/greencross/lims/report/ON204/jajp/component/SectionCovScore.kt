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

class SectionCovScore(private val y: Float = 669f): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACXTemplate<DNACXResource>?,
        dto: DNACXDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        var img = template!!.resource().imgShortSmallTitle()
        var width = img.width * SMALL_TITLE_RATE / img.height
        stream.drawImage(img, 75f - width / 2, y - SMALL_TITLE_RATE - 2, width, SMALL_TITLE_RATE)
        stream.paragraph(38f, y - SMALL_TITLE_RATE+2, 200f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().clone().fontSize(13f).color(Color(255,255,255)), "COVスコア"))
        stream.paragraph(120f, y- SMALL_TITLE_RATE+2, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().fontSize(11f).color(Color(114,113,113)), "COVスコアは、全ゲノムレベルでのカバレッジパターン(塩基配列のパターン)を示します。"))

        img = template.resource().imgCovScoreBackgorund()
        width = img.width * BACKGROUND_RATE / img.height
        stream.drawImage(img, 298f - width / 2, y - BACKGROUND_RATE - 55f, width, BACKGROUND_RATE)

        //0.00 x값 = 135f / 1.00 x값 = 545f
        //0.01 당 4.1
        stream.restoreGraphicsState()
        stream.saveGraphicsState()
        stream.setLineWidth(2f).setStrokingColor(Color(186,26,50)).setNonStrokingColor(Color(186,26,50))
            .setLineDashPattern(floatArrayOf(1.7f, 2f), 1f).moveTo(135f+(dto.result.covScoreCutOff.toFloat()*100f*4.1f), y - BACKGROUND_RATE - 26f).lineTo(135f+(dto.result.covScoreCutOff.toFloat()*100f*4.1f), y - BACKGROUND_RATE + 68f).stroke()
        stream.restoreGraphicsState()
        stream.saveGraphicsState()
        stream.setLineWidth(1.5f).setStrokingColor(Color(67,72,143)).setNonStrokingColor(Color(67,72,143))
            .setLineDashPattern(floatArrayOf(2f, 1.7f), 3f).moveTo(135f+(dto.result.covScore.toFloat()*100)*4.1f, y - BACKGROUND_RATE - 26f).lineTo(135f+(dto.result.covScore.toFloat()*100)*4.1f,y - BACKGROUND_RATE + 68f).stroke()
        stream.circle(135f+(dto.result.covScore.toFloat()*100)*4.1f,y - BACKGROUND_RATE + 68f, 2.5f).fill()
        stream.restoreGraphicsState()
        stream.saveGraphicsState()
        stream.paragraph(135f+(dto.result.covScore.toFloat()*100)*4.1f,y - BACKGROUND_RATE + 96f, 200f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(template.resource().styleContentBold().clone().color(Color(0,0,0)).fontSize(10f), "あなたのスコア"))
        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(135f+(dto.result.covScore.toFloat()*100)*4.1f-35, y - BACKGROUND_RATE + 88f).lineTo(135f+(dto.result.covScore.toFloat()*100)*4.1f+37f,y - BACKGROUND_RATE + 88f).stroke()
        stream.paragraph(135f+(dto.result.covScore.toFloat()*100)*4.1f,y - BACKGROUND_RATE + 79f, 200f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(template.resource().styleContentBold().clone().color(if(dto.result.covScore >= dto.result.covScoreCutOff) Color(186, 26, 50) else Color(111,186,44)).fontSize(14f), "${dto.result.covScore}"))
        stream.restoreGraphicsState()
        stream.saveGraphicsState()
        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0))
            .setLineDashPattern(floatArrayOf(1f, 1f), 2f).moveTo(35f, y-182f).lineTo(560f,y-182f).stroke()
        stream.restoreGraphicsState()
        return stream
    } companion object {
        private const val SMALL_TITLE_RATE = 18f
        private const val BACKGROUND_RATE = 122f
    }
}
