package com.greencross.lims.report.cancerch.GangbukComponent

import com.gcgenome.lims.report.TextBlock
import com.greencross.lims.report.builder.Util
import com.greencross.lims.report.builder.Util_EnUS
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.cancerch.CancerchResource
import com.greencross.lims.report.cancerch.CancerchTemplate
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.time.format.DateTimeFormatter

class GangbukSectionTitleEnUs(private val y: Float = 745f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
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

        stream.paragraph(215f, y + 50f,     70f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(6.5f), "Institution"))
        stream.paragraph(215f, y + 20.6f,   70f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(6.5f), "Registration No."))
        stream.paragraph(215f, y + 35.3f,   70f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(6.5f), "Name"))
        stream.paragraph(215f, y + 5.6f,    70f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(6.5f), "Age/Gender"))
        stream.paragraph(386f, y + 20.6f,   70f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(6.5f), "Receipt No."))
        stream.paragraph(386f, y + 5.6f,    70f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(6.5f), "Sample Type"))
        stream.paragraph(215f, y - 9.4f,    100f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(6.5f), "Sample Collection Date"))
        stream.paragraph(386f, y - 9.4f,    100f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(6.5f), "Receipt/Report Date"))

        stream.paragraph(297f, y + 50f,   300f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(6.5f), dto.medicalInstitution))
        stream.paragraph(297f, y + 20.6f, 150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(6.5f), dto.requestNumber))
        stream.paragraph(297f, y + 35.3f, 300f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(6.5f), dto.patientName))
        stream.paragraph(297f, y + 5.6f,  150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(6.5f), Util_EnUS.dashIfEmpty(dto.age!!)),
            TextBlock(template.resource().styleContentRegualar().fontSize(6.5f), " / "),
            TextBlock(template.resource().styleContentRegualar().fontSize(6.5f), Util_EnUS.dashIfEmpty(Util_EnUS.sex(dto.sex))))
        stream.paragraph(468f, y + 20.6f, 150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(6.5f), Util_EnUS.dashIfEmpty(dto.medicalRecordNumber!!)))
        stream.paragraph(468f, y + 5.6f, 150f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(6.5f), dto.specimenType))
        stream.paragraph(297f, y - 9.4f, 150f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(6.5f), Util_EnUS.dashIfEmpty(Util_EnUS.date(dto.collectionDate)!!)))
        stream.paragraph(468f, y - 9.4f, 150f,  AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(6.5f), Util_EnUS.dashIfEmpty(Util_EnUS.date(dto.receiptDate)!!)),
            TextBlock(template.resource().styleContentRegualar().fontSize(6.5f), " / "),
            TextBlock(template.resource().styleContentRegualar().fontSize(6.5f), Util.dashIfEmpty(Util_EnUS.date(dto.reportDate)!!)))
        //endregion

        stream.restoreGraphicsState()
        return stream
    }

    companion object {
        private const val TITLE_HEIGHT = 48f
        private const val TITLE_HEADERBOX_HEIGHT = 84f
    }
}
