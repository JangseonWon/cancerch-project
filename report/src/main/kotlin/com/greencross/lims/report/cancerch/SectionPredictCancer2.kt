package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.TextStyle
import com.greencross.lims.report.builder.Util_EnUS.Companion.cancerToEng
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionPredictCancer2(private val y: Float = 457f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        var img = template!!.resource().imgDoubtContentBox()
        var width = 527f
        var style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
        stream!!.saveGraphicsState()
        stream.drawImage(img, 298f - width / 2, y - DOUBT_SQUARE_RATE+10, width, 131f)
        stream.paragraph(
            562f - width / 2, y - DOUBT_SQUARE_RATE + 121, 300f, AlignHorizontal.CENTER,
            TextBlock(template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "Predicted Tumor of Origin")
        )
        //endregion

        //region □ Doubt Content's Text
        style = TextStyle().color(template.resource().colorText()).fonts(template.resource().fontHeader(), template.resource().fontDefault()).color(Color(67, 72, 142)).fontSize(12f)

        when(dto!!.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(566f - width / 2, y - DOUBT_SQUARE_RATE + 75, 400f, AlignHorizontal.CENTER, TextBlock(style, "Not applicable"))
            CancerchDto.Results.CONCERN -> stream.paragraph(566f - width / 2, y - DOUBT_SQUARE_RATE + 87, 600f, AlignHorizontal.CENTER, TextBlock(style, "Not Specified : "), TextBlock(template.resource().styleContentBold().color(Color(67, 72, 142)).fontSize(12f),"Follow-up is recommended"))
            CancerchDto.Results.RISK -> stream.paragraph(437f - width / 2, y - DOUBT_SQUARE_RATE + 80, 400f, AlignHorizontal.LEFT, TextBlock(style, "Most similar to the DNA patterns of [ "), TextBlock(template.resource().styleContentBold().color(Color(67, 72, 142)).fontSize(12f), (if (dto.first.name == "기타암종") "Other Cancers" else cancerToEng(dto.first.name))), TextBlock(style, " ]"))
        }
        style = template.resource().styleContentRegualar().clone().color(Color(72, 71, 71)).fontSize(9f)
        when(dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(566f - width / 2, y - DOUBT_SQUARE_RATE + 48, 300f, AlignHorizontal.CENTER, TextBlock(style, "The DNA patterns analysis shows low probability of cancer."))
            CancerchDto.Results.CONCERN -> {
                stream.paragraph(566f - width / 2, y - DOUBT_SQUARE_RATE + 62, 300f, AlignHorizontal.CENTER, TextBlock(style, "The possibility of cancer has been found, follow-up is recommended.\n" +
                        "This test predicts the most similar cancer type among 6 major cancers.\n" +
                        "Further analysis for other cancer types is not optimized."))
                stream.paragraph(
                    566f - width / 2, y - DOUBT_SQUARE_RATE + 25f, 300f, AlignHorizontal.CENTER, TextBlock(
                        style.clone().fontSize(7f), "* 6 cancer types: Lung, Colon, Liver, Pancreatobiliary, Esophageal, and Ovarian cancer."
                    )
                )
            }
            CancerchDto.Results.RISK -> {
                stream.paragraph(
                    437f - width / 2, y - DOUBT_SQUARE_RATE + 61f, 300f, AlignHorizontal.LEFT, TextBlock(
                        style, "This test predicts the most similar cancer type among 6 major cancers.\n" +
                                "Further analysis for other cancer types is not optimized."
                    )
                )
                stream.paragraph(
                    437f - width / 2, y - DOUBT_SQUARE_RATE + 31f, 300f, AlignHorizontal.LEFT, TextBlock(
                        style.clone().fontSize(7f), "*6 cancer types: Lung, Colon, Liver, Pancreatobiliary, Esophageal, and Ovarian cancer."
                    )
                )
            }
        }

        if (dto.result == CancerchDto.Results.RISK) {
            img = template.resource().imgDoubtCancer(dto.first.name)
            width = img.width * DOUBT_CANCER_RATE / img.height
            stream.drawImage(
                img, 105f - width / 2f, y - DOUBT_CANCER_RATE - 42, width,
                DOUBT_CANCER_RATE
            )
        }
        stream.restoreGraphicsState()
        return stream
    }

    companion object {
        private const val DOUBT_SQUARE_RATE = 141f
        private const val DOUBT_CANCER_RATE = 76f
    }
}
