package com.greencross.lims.report.cancerch.GangbukComponent

import com.gcgenome.lims.report.TextBlock
import com.greencross.lims.report.builder.Util
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.cancerch.CancerchResource
import com.greencross.lims.report.cancerch.CancerchTemplate
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.awt.Color

class GangbukSectionSimpleTitleKoKr(private val y: Float = 805f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        stream.paragraph(37f, y, 70f,  AlignHorizontal.LEFT, TextBlock(template!!.resource().styleContentBold().fontSize(8f), "성명"))
        stream.setLineWidth(0.7f).setStrokingColor(Color(81, 78, 145)).line(56f, y+6, 56f, y-1).stroke()
        stream.paragraph(60f,  y, 300f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8f), dto.patientName))

        stream.paragraph(332f, y,70f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(8f), "등록번호"))
        stream.line(366f, y+6, 366f, y-1).setLineWidth(0.7f).setStrokingColor(Color(81, 78, 145)).stroke()
        stream.paragraph(370f, y, 150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8f), dto.medicalRecordNumber))

        stream.paragraph(470f, y,70f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(8f), "검체채취일"))
        stream.line(511f, y+6, 511f, y-1).setLineWidth(0.7f).setStrokingColor(Color(81, 78, 145)).stroke()
        stream.paragraph(515f, y, 300f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8f), Util.dateOrDash(dto.collectionDate)))

        stream.restoreGraphicsState()
        return stream
    }

    companion object {
    }

}
