package com.greencross.lims.report.kokr

import com.greencross.lims.report.Template
import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.TextStyle
import com.greencross.lims.report.avoid.kokr.AvoidResourceKoKr
import com.greencross.lims.report.builder.AbstractReportDto
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.builder.Util
import com.greencross.lims.report.func.AlignVertical.MIDDLE
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters


class SectionHeader<T: Template<AvoidResourceKoKr>, D: AbstractReportDto>: Painter<T, D> {
    private val DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val lblMedicalInstitution = "의뢰기관"
    private val lblMedicalRecordNumber = "등록번호"
    private val lblRequestNumber = "접수번호"
    private val lblPatientName = "성명"
    private val lblPatientCode = "주민번호"
    private val lblAgeSex = "나이/성별"
    private val lblSpecimenType = "검체종류"
    private val lblWardDepartment = "병동/진료과"
    private val lblCollectionDate = "검체채취일"
    private val lblPatientInfo = "임상정보/기타"
    private val lblPhysician = "주치의"
    private val lblReceiptReportDate = "접수일/보고일"

    @Throws(IOException::class)
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: T,
        dto: D
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()
        val resource: HasHeaderKoKr = template.resource()
        val colorGray = resource.colorGray()
        val styleHeaderTitle =
            TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(9f)
                .justify(true).paragraph(false)
        val styleValueTitle =
            TextStyle().color(resource.colorText()).fonts(resource.fontHeaderValue(), resource.fontDefault())
                .fontSize(7f).paragraph(false)
        var y = stream.cursorY()
        stream.setNonStrokingColor(colorGray)
            .setLineWidth(0.25f).setStrokingColor(colorGray)
            .addRect(60f, y, 70f, -63f)
            .addRect(216f, y, 70f, -63f)
            .addRect(374f, y, 70f, -63f)
            .fill()
        y -= 10f
        stream.line(60f, y - 5, 533f, y - 5).stroke()
        stream.paragraph(62f, y, 65f, TextBlock(styleHeaderTitle, lblMedicalInstitution))
        stream.paragraph(219f, y, 65f, TextBlock(styleHeaderTitle, lblMedicalRecordNumber))
        stream.paragraph(377f, y, 65f, TextBlock(styleHeaderTitle, lblRequestNumber))
        stream.paragraph(
            135f,
            y + 2.5f,
            83f,
            MIDDLE,
            TextBlock(styleValueTitle, Util.dashIfEmpty(dto.medicalInstitution!!))
        )
        stream.paragraph(290f, y, 88f, TextBlock(styleValueTitle, Util.dashIfEmpty(dto.medicalRecordNumber!!)))
        y = stream.paragraph(447f, y, 100f, TextBlock(styleValueTitle, Util.dashIfEmpty(dto.requestNumber!!)))
        y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f
        stream.paragraph(62f, y, 65f, TextBlock(styleHeaderTitle, lblPatientName))
        stream.paragraph(219f, y, 65f, TextBlock(styleHeaderTitle, lblPatientCode))
        stream.paragraph(377f, y, 65f, TextBlock(styleHeaderTitle, lblAgeSex))
        stream.paragraph(135f, y, 86f, TextBlock(styleValueTitle, Util.dashIfEmpty(dto.patientName!!)))
        stream.paragraph(290f, y, 88f, TextBlock(styleValueTitle, Util.dashIfEmpty(dto.patientCode!!)))
        y = stream.paragraph(
            447f, y, 100f,
            TextBlock(styleValueTitle, Util.dashIfEmpty(age(dto.birthDate!!, dto.collectionDate!!))),
            TextBlock(styleValueTitle, " / "),
            TextBlock(styleValueTitle, Util.dashIfEmpty(sex(dto.sex!!)))
        )
        stream.line(60f, y - 5, 533f, y - 5)
        y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f
        stream.paragraph(62f, y, 65f, TextBlock(styleHeaderTitle, lblSpecimenType))
        stream.paragraph(219f, y, 65f, TextBlock(styleHeaderTitle, lblWardDepartment))
        stream.paragraph(377f, y, 65f, TextBlock(styleHeaderTitle, lblCollectionDate))
        stream.paragraph(135f, y, 86f, TextBlock(styleValueTitle, Util.dashIfEmpty(dto.specimenType!!)))
        stream.paragraph(
            290f, y, 88f,
            TextBlock(styleValueTitle, Util.dashIfEmpty(dto.ward!!)),
            TextBlock(styleValueTitle, " / "),
            TextBlock(styleValueTitle, Util.dashIfEmpty(dto.department!!))
        )
        y = stream.paragraph(447f, y, 100f, TextBlock(styleValueTitle, Util.dashIfEmpty(date(dto.collectionDate)!!)))
        stream.line(60f, y - 5, 533f, y - 5)
        y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f
        stream.paragraph(62f, y, 65f, TextBlock(styleHeaderTitle, lblPatientInfo))
        stream.paragraph(219f, y, 65f, TextBlock(styleHeaderTitle, lblPhysician))
        stream.paragraph(377f, y, 65f, TextBlock(styleHeaderTitle, lblReceiptReportDate))
        stream.paragraph(135f, y, 86f, TextBlock(styleValueTitle, Util.dashIfEmpty(dto.patientInfo!!)))
        stream.paragraph(290f, y, 88f, TextBlock(styleValueTitle, Util.dashIfEmpty(dto.physician!!)))
        y = stream.paragraph(
            447f, y, 100f,
            TextBlock(styleValueTitle, Util.dashIfEmpty(date(dto.receiptDate)!!)),
            TextBlock(styleValueTitle, " / "),
            TextBlock(styleValueTitle, Util.dashIfEmpty(date(dto.reportDate)!!))
        )
        stream.line(60f, y - 5, 533f, y - 5).stroke()
        stream.restoreGraphicsState()
        stream.cursorY(y)
        return stream
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
            else -> "-"
        }
    }
}