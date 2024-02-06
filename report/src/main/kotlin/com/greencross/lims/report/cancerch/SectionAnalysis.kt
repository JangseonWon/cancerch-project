package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionAnalysis(private val y: Float = 200f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.line(35f, y+113, 560f, y+113).setStrokingColor(Color(67, 72, 142)).setLineWidth(1f).stroke()
        stream.line(35f, y+67, 560f, y+67).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()
        stream.line(35f, y-98, 560f, y-98).setStrokingColor(Color(67, 72, 142)).setLineWidth(1f).stroke()
        stream.line(195f, y+113, 195f, y-98).setStrokingColor(Color.BLACK).setLineWidth(0.2f).stroke()


        template!!.lblDetailResultAnalysisTableHeaderTop(stream, y)
        template.lblDetailResultAnalysisTableContentTop(stream, y, dto!!)
        template.lblDetailResultAnalysisTableHeaderBot(stream, y, dto)
        template.lblDetailResultAnalysisTableContentBot(stream, y, dto)
        return stream
    }
}