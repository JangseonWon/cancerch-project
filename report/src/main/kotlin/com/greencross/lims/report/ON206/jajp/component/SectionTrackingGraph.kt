package com.greencross.lims.report.ON206.jajp.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import java.awt.Color
import java.time.LocalDate

class SectionTrackingGraph(private val y: Float = 480f) : Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: DNACTTemplate<DNACTResource>,
        dto: DNACTDto
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()
        stream.paragraph(35f, y-20f, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().clone().color(Color(0,80,109)).fontSize(15f), "追跡グラフ"))

        var dateAndValue = dto.result.sortedBy{it.date}.map { item -> Triple(item.date, item.cfDNAConcentration, item.cfDNAConcentrationCutOff.toFloat()) }
        drawGraph(stream, template, 154f, y-40, dateAndValue, "cfconc")

        dateAndValue = dto.result.sortedBy{it.date}.map { item -> Triple(item.date, item.genomicInstability, item.genomicInstabilityCutOff.toFloat()) }
        drawGraph(stream, template, 442f, y-40, dateAndValue, "iscore")

        dateAndValue = dto.result.sortedBy{it.date}.map { item -> Triple(item.date, item.covScore, item.covScoreCutOff.toFloat()) }
        drawGraph(stream, template, 154f, y-210, dateAndValue, "cov")

        dateAndValue = dto.result.sortedBy{it.date}.map { item -> Triple(item.date, item.femsScore, item.femsScoreCutOff.toFloat()) }
        drawGraph(stream, template, 442f, y-210, dateAndValue, "fems")

        stream.restoreGraphicsState()
        return stream
    }
    private fun drawGraph(stream: PDPageContentStreamPageAccessible, template: DNACTTemplate<DNACTResource>, x: Float, y: Float, dateAndValue: List<Triple<LocalDate, Float, Float>>, type: String) {
        val img = template.resource().imgTrackingGraph(type)
        val width = img.width * GRAPH_RATE / img.height
        val font = template.resource().styleContentRegualar().fontSize(7f).color(Color(0,0,0))
        stream.drawImage(img, x - width / 2, y- GRAPH_RATE, width, GRAPH_RATE)

        // y-42  = 100%
        // y-146 = 0%
        val max = when(type) {
            "cfconc" -> 50f
            "iscore" -> 10f
            "cov", "fems" -> 1f
            else -> throw Exception("이상한 값이 입력됨")
        }

        val cutOffY = y-146 + (dateAndValue[0].third / max * 100)*1f
        val lineList: MutableList<Triple<Float, Float, Float>> = mutableListOf()
        stream.setLineWidth(1f).setStrokingColor(Color(186,26,50)).setNonStrokingColor(Color(186,26,50))
            .setLineDashPattern(floatArrayOf(1.7f, 2f), 1f).moveTo(x-width/2+23,cutOffY).lineTo(x+width/2,cutOffY).stroke()
        stream.restoreGraphicsState()
        for (i in 1..dateAndValue.size) lineList.add(Triple(x-width/2 + 16 + i*(width - 16)/(dateAndValue.size+1), y-146 + (dateAndValue[i-1].second / max * 100)*1f, dateAndValue[i-1].third))

        for (i in 0..lineList.size-2) {
            stream.setLineWidth(1f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0))
                .moveTo(lineList[i].first+2.5f, lineList[i].second+2.5f).lineTo(lineList[i+1].first+2.5f, lineList[i+1].second+2.5f).stroke()
        }
        for (i in 0 .. dateAndValue.size-1) {
            val color = if(dateAndValue[i].second <= dateAndValue[i].third) Color(0,0,0) else Color(186,26,50)
            stream.paragraph(lineList[i].first+2.5f, y - GRAPH_RATE - 10, 100f, AlignHorizontal.CENTER, TextBlock(font, dateAndValue[i].first.toString()))
            stream.roundRect(lineList[i].first, lineList[i].second, 5f, 5f, 2.5f).setLineWidth(2f).setStrokingColor(color).setNonStrokingColor(Color(255,255,255)).fillAndStroke()
        }
        stream.saveGraphicsState()

    }
    companion object {
        private const val GRAPH_RATE = 150f
    }
}
