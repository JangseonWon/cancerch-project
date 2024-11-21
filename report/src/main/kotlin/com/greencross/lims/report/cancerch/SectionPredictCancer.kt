package com.greencross.lims.report.cancerch

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.TextStyle
import com.greencross.lims.report.builder.Util
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.awt.Color

class SectionPredictCancer(private val y: Float = 457f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
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
            TextBlock(template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "6종 암 중 인공지능 예측 암종")
        )
        //endregion

        //region □ Doubt Content's Text
        style = TextStyle().color(template.resource().colorText()).fonts(template.resource().fontHeader(), template.resource().fontDefault()).color(Color(67, 72, 142)).fontSize(13f)

        when(dto!!.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(423f - width / 2 + 130, y - DOUBT_SQUARE_RATE + 75, 400f, AlignHorizontal.CENTER, TextBlock(style, "해당없음"))
            CancerchDto.Results.CONCERN -> stream.paragraph(423f - width / 2 + 130, y - DOUBT_SQUARE_RATE + 75, 400f, AlignHorizontal.CENTER, TextBlock(style, "해당없음 : 추적관찰 권장"))
            CancerchDto.Results.RISK -> when(dto.first.name) {
                "기타암종" ->stream.paragraph(323f - width / 2 + 130, y - DOUBT_SQUARE_RATE + 80, 400f, AlignHorizontal.LEFT, TextBlock(style, "암종 예측 불가: 6종 암 또는 기타 암의 존재 가능성이 있습니다."))
                else -> stream.paragraph(323f - width / 2 + 130, y - DOUBT_SQUARE_RATE + 80, 400f, AlignHorizontal.LEFT, TextBlock(style, "6종 암 중 " + Util.lblCancerToWord(dto!!.first.name) + "의 DNA 패턴과 가장 유사합니다."))
            }
        }
        style = template.resource().styleContentRegualar().clone().color(Color(0, 0, 0)).fontSize(10f)
        when(dto.result) {
            CancerchDto.Results.GENERAL -> stream.paragraph(423f - width / 2 + 130, y - DOUBT_SQUARE_RATE + 48, 300f, AlignHorizontal.CENTER, TextBlock(style, "DNA 패턴 분석 결과, 암 존재 가능성이 낮게 예측되었습니다."))
            CancerchDto.Results.CONCERN -> stream.paragraph(423f - width / 2 + 130, y - DOUBT_SQUARE_RATE + 48, 400f, AlignHorizontal.CENTER, TextBlock(style, "특정 암으로 예측하기에는 불분명하나 암 존재 가능성이 약 2배 이상 높을 것으로 예측됩니다."))
            CancerchDto.Results.RISK -> when(dto.first.name){
                "기타암종" -> stream.paragraph(323f - width / 2 + 130, y - DOUBT_SQUARE_RATE + 63, 300f, AlignHorizontal.LEFT, TextBlock(style, "특정 암으로 예측하기에는 불분명하나 암 존재 가능성이\n" +
                        "약 10배 이상 높을 것으로 예측됩니다.\n\n"),
                    TextBlock(style.clone().fontSize(8f), "*6종 암: 폐암, 대장암, 간암, 췌장담도암, 식도암, 난소암"))
                else -> stream.paragraph(323f - width / 2 + 130, y - DOUBT_SQUARE_RATE + 63, 300f, AlignHorizontal.LEFT, TextBlock(style, "본 검사에 포함된 6종 암 중 가장 유사한 암종에 대해 예측하므로,\n" +
                        "타 암종에 대해서는 정확한 분석이 어렵습니다.\n\n"),
                    TextBlock(style.clone().fontSize(8f), "*6종 암: 폐암, 대장암, 간암, 췌장담도암, 식도암, 난소암"))
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
