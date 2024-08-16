package com.greencross.lims.report.cancerch.GangbukComponent

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.builder.Util
import com.greencross.lims.report.builder.Util_EnUS
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.cancerch.CancerchResource
import com.greencross.lims.report.cancerch.CancerchTemplate
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.AlignVertical
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color
import java.time.format.DateTimeFormatter

class GangbukSectionSimpleTitleEnUs(private val y: Float = 805f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    private val DTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ HeaderBox, 내부 내용
        stream.paragraph(37f, y, 70f,  AlignHorizontal.LEFT, TextBlock(template!!.resource().styleContentBold().fontSize(8f), "Name"))
        stream.line(63f, y+6, 63f, y-1).setLineDashPattern(floatArrayOf(0f, 0f), 0f).setLineWidth(0.7f).setStrokingColor(Color(81, 78, 145)).stroke()
        stream.paragraph(67f,  y, 300f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8f), dto.patientName))

        stream.paragraph(300f, y,70f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(8f), "Registration No."))
        stream.line(366f, y+6, 366f, y-1).setLineDashPattern(floatArrayOf(0f, 0f), 0f).setLineWidth(0.7f).setStrokingColor(Color(81, 78, 145)).stroke()
        stream.paragraph(370f, y, 150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8f), dto.medicalRecordNumber))

        stream.paragraph(454f, y,70f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(8f), "Collction Date"))
        stream.line(511f, y+6, 511f, y-1).setLineDashPattern(floatArrayOf(0f, 0f), 0f).setLineWidth(0.7f).setStrokingColor(Color(81, 78, 145)).stroke()
        stream.paragraph(515f, y, 300f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8f), Util_EnUS.dateOrDash(dto.collectionDate)))

        stream.restoreGraphicsState()
        return stream
    }

    companion object {
    }
}
