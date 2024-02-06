package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.builder.Util
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class SectionTitle(private val y: Float = 745f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    private val DTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        //region □ Header 좌측 로고, 아래 설명
        var img = template!!.resource().imgTitle()
        var width = img.width * TITLE_HEIGHT / img.height
        stream.drawImage(img, 110f - width / 2, y-1, width, TITLE_HEIGHT)
        //endregion

        //region □ HeaderBox, 내부 내용
        img = template.resource().imgHeaderBox()
        width = img.width * TITLE_HEADERBOX_HEIGHT / img.height
        stream.drawImage(img, 383f - width / 2, y - 19, 354f, TITLE_HEADERBOX_HEIGHT)

        template.lblMedicalInstitution(stream, y)
        template.lblRequestNumber(stream, y)
        template.lblPatientName(stream, y)
        template.lblAgeSex(stream, y)
        template.lblMedicalRecordNumber(stream, y)
        template.lblSpecimenType(stream, y)
        template.lblSpecimenDate(stream, y)
        template.lblReceiptReportDate(stream, y)

        template.lblMedicalInstitution(stream, y, dto)
        template.lblRequestNumber(stream, y, dto)
        template.lblPatientName(stream, y, dto)
        template.lblAgeSex(stream, y, dto)
        template.lblMedicalRecordNumber(stream, y, dto)
        template.lblSpecimenType(stream, y, dto)
        template.lblSpecimenDate(stream, y, dto)
        template.lblReceiptReportDate(stream, y, dto)
        //endregion

        stream.restoreGraphicsState()
        return stream.cursorY(y - TITLE_HEIGHT)
    }

    companion object {
        private const val TITLE_HEIGHT = 48f
        private const val TITLE_HEADERBOX_HEIGHT = 84f
    }
}