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

class SectionSignalScore(private val y: Float = 379f): Painter<DNACXTemplate<DNACXResource>, DNACXDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: DNACXTemplate<DNACXResource>?,
        dto: DNACXDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template!!.resource().styleContentBold().clone().color(Color(67,72,142)).fontSize(15f), "シグナルスコア"))
        stream.paragraph(35f, y-35f, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(114,113,113)).fontSize(12f), "シグナルスコアは、cfDNAのパターンを分析することにより、がんDNAのシグナルを示します。\n" +
                "シグナルスコアのカットオフは、年齢グループによって異なります。"))

        val img = template.resource().imgSignalScoreBackground()
        val width = img.width * BACKGROUND_RATE / img.height
        stream.drawImage(img, 298f - width / 2, y - BACKGROUND_RATE - 55, width, BACKGROUND_RATE)

        //0.00 x값 = 137f / 1.00 x값 = 479f
        //0.01 당 3.42f 증가 0.38 일 경우 38*3.42 = 129.96f + 137ff
        stream.restoreGraphicsState()
        stream.saveGraphicsState()
        stream.setLineWidth(2f).setStrokingColor(Color(186,26,50)).setNonStrokingColor(Color(186,26,50))
            .setLineDashPattern(floatArrayOf(1.7f, 2f), 1f).moveTo(137f+(dto.result.signalScore95CutOff.toFloat()*100)*3.42f, y - BACKGROUND_RATE-14).lineTo(137f+(dto.result.signalScore95CutOff.toFloat()*100)*3.42f, y - BACKGROUND_RATE + 126f).stroke()
        stream.restoreGraphicsState()
        stream.saveGraphicsState()
        stream.setLineWidth(1.5f).setStrokingColor(Color(67,72,143)).setNonStrokingColor(Color(67,72,143))
            .setLineDashPattern(floatArrayOf(2f, 1.7f), 3f).moveTo(137f+(dto.result.signalScore.toFloat()*100)*3.42f, y - BACKGROUND_RATE-14).lineTo(137f+(dto.result.signalScore.toFloat()*100)*3.42f,y - BACKGROUND_RATE + 126f).stroke()
        stream.circle(137f+(dto.result.signalScore.toFloat()*100)*3.42f,y - BACKGROUND_RATE + 126f, 2.5f).fill()
        stream.restoreGraphicsState()
        stream.saveGraphicsState()
        stream.paragraph(137f+(dto.result.signalScore.toFloat()*100)*3.42f,y - BACKGROUND_RATE + 158f, 200f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(template.resource().styleContentBold().clone().color(Color(0,0,0)).fontSize(12f), "あなたのスコア"))
        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(137f+(dto.result.signalScore.toFloat()*100)*3.42f-45, y - BACKGROUND_RATE + 149f).lineTo(137f+(dto.result.signalScore.toFloat()*100)*3.42f+47f,y - BACKGROUND_RATE + 149f).stroke()
        stream.paragraph(137f+(dto.result.signalScore.toFloat()*100)*3.42f,y - BACKGROUND_RATE + 140f, 200f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(template.resource().styleContentBold().clone().color(if(dto.result.signalScore >= dto.result.signalScore95CutOff) Color(186, 26, 50) else Color(111,186,44)).fontSize(16f), "${dto.result.signalScore}"))
        stream.restoreGraphicsState()
        return stream
    }
    companion object {
        private const val BACKGROUND_RATE = 240f
    }
}
