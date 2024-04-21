package com.greencross.lims.report.avoid

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


class SectionTitle (private val y: Float = 745f) : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    private val DTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: AvoidTemplate<AvoidResource>,
        dto: AvoidDto
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()

        //region □ Header 좌측 로고, 아래 설명
        var img = template.resource().imgTitle()
        var width = img.width * TITLE_HEIGHT / img.height
        stream.drawImage(img, 134f - width/2, y-10, width, TITLE_HEIGHT)
        //endregion

        //region □ HeaderBox, 내부 내용
        img = template.resource().imgHeaderBox()
        width = img.width * TITLE_HEADERBOX_HEIGHT / img.height
        stream.drawImage(img, 385f - width/2, y-25, width, TITLE_HEADERBOX_HEIGHT)

        stream.paragraph(235f, y+35, 50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold(), "의뢰기관"))
        stream.paragraph(385f, y+35, 50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold(), "접수번호" ))
        stream.paragraph(235f, y+20, 50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold(),  "성명"))
        stream.paragraph(385f, y+20, 50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold(), "나이/성별"))
        stream.paragraph(235f,  y+5, 50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold(),"등록번호"))
        stream.paragraph(385f,  y+5, 50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold(), "검체종류"))
        stream.paragraph(235f, y-10, 50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold(),"검체채취일"))
        stream.paragraph(385f, y-10, 50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold(),"접수일/보고일"))


        var style = template.resource().styleContentRegualar()
        stream.paragraph(285f, y+35, 100f, AlignHorizontal.LEFT, TextBlock(style, dto.medicalInstitution))
        stream.paragraph(445f, y+35, 100f, AlignHorizontal.LEFT, TextBlock(style, dto.requestNumber))
        stream.paragraph(285f, y+20, 100f, AlignHorizontal.LEFT, TextBlock(style, dto.patientName))
        stream.paragraph(445f, y+20, 100f, AlignHorizontal.LEFT,
            TextBlock(style, Util.dashIfEmpty(dto.age!!)),
            TextBlock(style, " / "),
            TextBlock(style, Util.dashIfEmpty(sex(dto.sex)))
        )
        stream.paragraph(285f, y+5, 100f, AlignHorizontal.LEFT, TextBlock(style, Util.dashIfEmpty(dto.medicalRecordNumber!!)))
        stream.paragraph(445f, y+5, 100f, AlignHorizontal.LEFT, TextBlock(style, dto.specimenType))
        stream.paragraph(285f, y-10, 100f, AlignHorizontal.LEFT, TextBlock(style, Util.dashIfEmpty(date(dto.collectionDate)!!)))
        stream.paragraph(445f, y-10, 100f, AlignHorizontal.LEFT,
            TextBlock(style, Util.dashIfEmpty(date(dto.receiptDate)!!)),
            TextBlock(style, " / "),
            TextBlock(style, Util.dashIfEmpty(date(dto.reportDate)!!))
        )
        //endregion

        stream.restoreGraphicsState()
        return stream.cursorY(y - TITLE_HEIGHT)
    }

    companion object {
        private const val TITLE_HEIGHT = 46f
        private const val TITLE_HEADERBOX_HEIGHT = 78f
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
