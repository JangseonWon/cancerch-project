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
        var style = template.resource().styleContentBold().fontSize(8.2f)
        stream.paragraph(222.5f, y + 45.5f, 50f, AlignHorizontal.LEFT, TextBlock(style, template.lblMedicalInstitution()))
        stream.paragraph(382.5f, y + 45.5f, 50f, AlignHorizontal.LEFT, TextBlock(style, template.lblRequestNumber()))
        stream.paragraph(222.5f, y + 28f, 50f, AlignHorizontal.LEFT, TextBlock(style, template.lblPatientName()))
        stream.paragraph(382.5f, y + 28f, 50f, AlignHorizontal.LEFT, TextBlock(style, template.lblAgeSex()))
        stream.paragraph(222.5f, y + 11f, 50f, AlignHorizontal.LEFT, TextBlock(style, template.lblMedicalRecordNumber()))
        stream.paragraph(382.5f, y + 11f, 50f, AlignHorizontal.LEFT, TextBlock(style, template.lblSpecimenType()))
        stream.paragraph(222.5f, y - 6, 50f, AlignHorizontal.LEFT, TextBlock(style, template.lblSpecimenDate()))
        stream.paragraph(382.5f, y - 6, 50f, AlignHorizontal.LEFT, TextBlock(style, template.lblReceiptReportDate()))

        style = template.resource().styleContentRegualar()
        stream.paragraph(285f, y + 45.5f, 100f, AlignHorizontal.LEFT, TextBlock(style, dto.medicalInstitution))
        stream.paragraph(445f, y + 45.5f, 100f, AlignHorizontal.LEFT, TextBlock(style, dto.requestNumber))
        stream.paragraph(285f, y + 28f, 100f, AlignHorizontal.LEFT, TextBlock(style, dto.patientName))
        stream.paragraph(
            445f, y + 28f, 100f, AlignHorizontal.LEFT,
            TextBlock(style, Util.dashIfEmpty(dto.age!!)),
            TextBlock(style, " / "),
            TextBlock(style, Util.dashIfEmpty(sex(dto.sex)))
        )
        stream.paragraph(
            285f, y + 11f, 100f,
            AlignHorizontal.LEFT, TextBlock(style, Util.dashIfEmpty(dto.medicalRecordNumber!!))
        )
        stream.paragraph(445f, y + 11f, 100f, AlignHorizontal.LEFT, TextBlock(style, dto.specimenType))
        stream.paragraph(
            285f, y - 6, 100f,
            AlignHorizontal.LEFT, TextBlock(style, Util.dashIfEmpty(date(dto.collectionDate)!!))
        )
        stream.paragraph(
            445f, y - 6, 100f, AlignHorizontal.LEFT,
            TextBlock(style, Util.dashIfEmpty(date(dto.receiptDate)!!)),
            TextBlock(style, " / "),
            TextBlock(style, Util.dashIfEmpty(date(dto.reportDate)!!))
        )
        //endregion

        stream.restoreGraphicsState()
        return stream.cursorY(y - TITLE_HEIGHT)
    }

    companion object {
        private const val TITLE_HEIGHT = 48f
        private const val TITLE_HEADERBOX_HEIGHT = 84f
    }

    fun date(date: LocalDate?): String? {
        return if (date == null) null else DTF.format(date)
    }

    fun date(date: LocalDateTime?): String? {
        return if (date == null) null else DTF.format(date)
    }

    fun age(birth: LocalDate?, sampling: LocalDate?): String {
        if (birth == null) return "-"
        return if (sampling == null) (Period.between(
            birth,
            LocalDate.now().with(TemporalAdjusters.firstDayOfYear())
        ).years + 1).toString() else (Period.between(
            birth,
            sampling.with(TemporalAdjusters.firstDayOfYear())
        ).years + 1).toString()
    }

    fun sex(sex: Sex?): String {
        return if (sex == null) "-" else when (sex) {
            Sex.M -> "남"
            Sex.F -> "여"
        }
    }
}