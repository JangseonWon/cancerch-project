package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.builder.Util.Companion.lblResultToWord
import com.greencross.lims.report.builder.Util.Companion.lblPatientInfo
import com.greencross.lims.report.builder.Util.Companion.lblPatientInfoWithCancer
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color
import kotlin.math.round

class SectionAnalysisComment(private val y: Float = 0f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {

    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        stream.line(47f,493f, 542f, 493f).setLineWidth(0.4f).setStrokingColor(Color(67, 72, 142)).stroke()
        if (CancerchDto.Results.GENERAL == dto!!.result) {
            val styleRegular = template!!.resource().styleContentRegualar().clone().fontSize(9f)
            val styleBold = template.resource().styleContentBold().clone().fontSize(9f)
            val blackbold = styleBold.clone().color(Color(11, 11, 11)).fontSize(9f)

            stream.paragraph(
                52f,
                y + RESULT_IMAGE_COMMENT_RATE + 275,
                480f,
                TextBlock(styleRegular, "아이캔서치 검사 결과 "),
                TextBlock(styleBold, lblResultToWord(dto.result)+"군"),
                TextBlock(styleRegular, "인 "),
                TextBlock(styleBold, "${dto.patientName}"),
                TextBlock(styleRegular, "님은 건강인의 DNA패턴과 유사합니다.\n단, 아이캔서치 검사는 모든 암을 검출할 수 없으며, 암의 병기나 종류에 따라 검출 성능이 달라질 수 있습니다.\n본 검사는 수검자의 암 존재 가능성을 확인하는 검사로 정확한 진단을 위한 검사는 아니며,\n확진을 위해서는 의료진 상담을 통한 정밀검사를 권장합니다.\n\n"),
                TextBlock(blackbold, "현재 암 일반관리군으로 분류된 것이 미래에 암이 발병하지 않음을 의미하는 것은 아니므로,\n정기적인 건강검진과 생활습관 관리를 통해 암을 예방할 것을 권장합니다.")
            )
        } else if (CancerchDto.Results.CONCERN == dto.result){
            val RESULT_IMAGE_COMMENT_RATE = 201f
            val styleRegular = template!!.resource().styleContentRegualar().clone().fontSize(9f)
            val styleBold = template.resource().styleContentBold().clone().fontSize(9f)
            val blackbold = styleBold.clone().color(Color(11, 11, 11)).fontSize(9f)

            stream.paragraph(
                52f,
                y + RESULT_IMAGE_COMMENT_RATE + 275,
                480f,
                AlignHorizontal.LEFT,
                TextBlock(styleRegular, "아이캔서치 검사 결과 "),
                TextBlock(styleBold, lblResultToWord(dto.result)+"군"),
                TextBlock(styleRegular, "인 "),
                TextBlock(styleBold, "${dto.patientName}"),
                TextBlock(styleRegular, "님은 암 환자의 이상 DNA패턴과 다소 유사합니다.\n혈액 속 암세포에서 유래된 DNA를 인공지능 알고리즘을 통해 분석한 결과, "),
                TextBlock(styleBold, "${dto.patientName}"),
                TextBlock(styleRegular, "님은 암의 존재 가능성이\n"),
                TextBlock(styleBold, "약 2배 이상 "),
                TextBlock(styleRegular, "높을 것으로 예측되나, 암 종을 예측하기에는 불분명합니다.\n\n관심관리 대상자여도 암으로 확진되기까지 수 개월이 걸릴 수도 있으므로 추적관찰을 요합니다.\n또한 "),
                TextBlock(blackbold, "건강인이라도 건강상태(양성질환, 자가면역질환 등)에 따라 관심관리 대상자로 보고될 수 있습니다.(약 5%).")
            )
        }

        else {
            if (dto.first.name != "기타암종"){
                val styleRegular = template!!.resource().styleContentRegualar().clone().fontSize(9f)
                val styleBold = template.resource().styleContentBold().clone().fontSize(9f)
                val blackbold = styleBold.clone().color(Color(11, 11, 11)).fontSize(9f)

                stream.paragraph(
                    52f,
                    y + RESULT_IMAGE_COMMENT_RATE + 275,
                    480f,
                    AlignHorizontal.LEFT,
                    TextBlock(styleRegular, "아이캔서치 검사 결과 "),
                    TextBlock(styleBold, lblResultToWord(dto.result)+"군"),
                    TextBlock(styleRegular, "인 "),
                    TextBlock(styleBold, dto.patientName),
                    TextBlock(styleRegular, "님은 "),
                    TextBlock(styleBold, dto.first.name),
                    TextBlock(styleRegular, " 환자의 이상 DNA패턴과 가장 유사합니다.\n혈액 속 암세포에서 유래된 DNA를 인공지능 알고리즘을 통해 분석한 결과, "),
                    TextBlock(styleBold, dto.patientName),
                    TextBlock(styleRegular, "님은 암의 존재 가능성이 다소 높을 것으로\n예측되어 일반인 대비 "),
                    TextBlock(styleBold, dto.first.name),
                    TextBlock(styleRegular, " 존재 가능성이 약 10배 이상 높을 것으로 예측됩니다.\n일반적으로 "),
                    TextBlock(styleBold, lblPatientInfoWithCancer(dto.age!!, dto.sex!!, dto.first.name)),
                    TextBlock(styleRegular, " 환자는 "),
                    TextBlock(styleBold, String.format("%.20f", (round(dto.first.asr.div(1000) * 10000) / 10000)).trimEnd('0').trimEnd('.') + "%"),
                    TextBlock(styleRegular, "(10만명 중에 "),
                    TextBlock(styleBold, dto.first.asr.toString()+"명"),
                    TextBlock(styleRegular, ")의 확률로 발생하지만,\n"),
                    TextBlock(styleBold, dto.patientName),
                    TextBlock(styleRegular, "님이 "),
                    TextBlock(styleBold, dto.first.name),
                    TextBlock(styleRegular, "일 확률은 약 "),
                    TextBlock(styleBold, dto.first.ppv.toString() + "%"),
                    TextBlock(styleRegular, "입니다.\n\n집중관리 대상자여도 암으로 확진되기까지 수 개월이 걸릴 수도 있으므로 추적관찰을 요합니다.\n또한 "),
                    TextBlock(blackbold,"건강인이라도 건강상태(양성질환, 자가면역질환 등)에 따라 집중관리 대상자로 보고될 수 있습니다(약 1%).")
                )
            }
            else {
                val styleRegular = template!!.resource().styleContentRegualar().clone().fontSize(9f)
                val styleBold = template.resource().styleContentBold().clone().fontSize(9f)
                val blackbold = styleBold.clone().color(Color(11, 11, 11)).fontSize(9f)

                stream.paragraph(
                    52f,
                    y + RESULT_IMAGE_COMMENT_RATE + 275,
                    480f,
                    AlignHorizontal.LEFT,
                    TextBlock(styleRegular, "아이캔서치 검사 결과 "),
                    TextBlock(styleBold, lblResultToWord(dto.result) + "군"),
                    TextBlock(styleRegular, "인 "),
                    TextBlock(styleBold, dto.patientName),
                    TextBlock(styleRegular, "님은 "),
                    TextBlock(styleBold, "암"),
                    TextBlock(styleRegular, " 환자의 이상 DNA패턴과 가장 유사합니다.\n혈액 속 암세포에서 유래된 DNA를 인공지능 알고리즘을 통해 분석한 결과, "),
                    TextBlock(styleBold, dto.patientName),
                    TextBlock(styleRegular, "님은 암의 존재 가능성이 다소 높을 것으로\n예측되어 일반인 대비 "),
                    TextBlock(styleBold, "암"),
                    TextBlock(styleRegular, " 존재 가능성이 약 10배 이상 높을 것으로 예측됩니다.\n일반적으로 "),
                    TextBlock(styleBold, lblPatientInfoWithCancer(dto.age!!, dto.sex!!, dto.first.name)),
                    TextBlock(styleRegular, " 환자는 "),
                    TextBlock(styleBold, String.format("%.20f", (round(dto.first.asr.div(1000) * 10000) / 10000)).trimEnd('0').trimEnd('.') + "%"),
                    TextBlock(styleRegular, "(10만명 중에 "),
                    TextBlock(styleBold, dto.first.asr.toString() + "명"),
                    TextBlock(styleRegular, ")의 확률로 발생하지만,\n"),
                    TextBlock(styleBold, dto.patientName),
                    TextBlock(styleRegular, "님이 "),
                    TextBlock(styleBold, "암"),
                    TextBlock(styleRegular, "일 확률은 약 "),
                    TextBlock(styleBold, dto.first.ppv.toString() + "%"),
                    TextBlock(styleRegular, "입니다.\n\n집중관리 대상자여도 암으로 확진되기까지 수 개월이 걸릴 수도 있으므로 추적관찰을 요합니다.\n또한 "),
                    TextBlock(blackbold, "건강인이라도 건강상태(양성질환, 자가면역질환 등)에 따라 집중관리 대상자로 보고될 수 있습니다(약 1%).\n\n"),
                    TextBlock(blackbold, dto.first.comment)
                )
            }
        }
        return stream
    }

    companion object {
        private const val RESULT_IMAGE_COMMENT_RATE = 201f
    }
}
