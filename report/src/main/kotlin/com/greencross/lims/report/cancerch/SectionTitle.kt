package com.greencross.lims.report.cancerch

import com.gcgenome.lims.report.TextBlock
import com.greencross.lims.report.builder.Util
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.time.format.DateTimeFormatter

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

        stream.paragraph(222.5f, y + 45.5f, 50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(8.2f), "의뢰기관"))
        stream.paragraph(382.5f, y + 45.5f, 50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(8.2f), "접수번호"))
        stream.paragraph(222.5f, y + 28f,   50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(8.2f), "성명"))
        stream.paragraph(382.5f, y + 28f,   50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(8.2f), "나이/성별"))
        stream.paragraph(222.5f, y + 11f,   50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(8.2f), "등록번호"))
        stream.paragraph(382.5f, y + 11f,   50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(8.2f), "검체종류"))
        stream.paragraph(222.5f, y - 6,     50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(8.2f), "검체채취일"))
        stream.paragraph(382.5f, y - 6,     50f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().fontSize(8.2f), "접수일/보고일"))

        stream.paragraph(285f, y + 45.5f,   150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8.2f), dto.medicalInstitution))
        stream.paragraph(445f, y + 45.5f,   150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8.2f), dto.requestNumber))
        stream.paragraph(285f, y + 28f,     300f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8.2f), dto.patientName))
        stream.paragraph(445f, y + 28f,     150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8.2f), Util.dashIfEmpty(dto.age!!)),
            TextBlock(template.resource().styleContentRegualar().fontSize(8.2f), " / "),
            TextBlock(template.resource().styleContentRegualar().fontSize(8.2f), Util.dashIfEmpty(Util.sex(dto.sex))))
        stream.paragraph(285f, y + 11f,     150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8.2f), Util.dashIfEmpty(dto.medicalRecordNumber!!)))
        stream.paragraph(445f, y + 11f,     150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8.2f), dto.specimenType))
        stream.paragraph(285f, y - 6f,      150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8.2f), Util.dashIfEmpty(Util.date(dto.collectionDate)!!)))
        stream.paragraph(445f, y - 6f,      150f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().fontSize(8.2f), Util.dashIfEmpty(Util.date(dto.receiptDate)!!)),
            TextBlock(template.resource().styleContentRegualar().fontSize(8.2f), " / "),
            TextBlock(template.resource().styleContentRegualar().fontSize(8.2f), Util.dashIfEmpty(Util.date(dto.reportDate)!!)))
        //endregion

        stream.restoreGraphicsState()
        return stream
    }

    companion object {
        private const val TITLE_HEIGHT = 48f
        private const val TITLE_HEADERBOX_HEIGHT = 84f
    }

}
