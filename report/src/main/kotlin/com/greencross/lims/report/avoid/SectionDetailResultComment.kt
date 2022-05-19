package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.builder.Sex
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import kotlin.math.round

class SectionDetailResultComment (private var y: Float = 581f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()



        var rate = if(AvoidDto.Results.저위험 == dto!!.result) RESULT_IMAGE_COMMENT_RATE_LOW else RESULT_IMAGE_COMMENT_RATE
        y = y - rate - 10
        var img = template!!.resource().imgAnalysisContentBox()
        var width = img.width*RESULT_IMAGE_COMMENT_RATE /img.height
        stream.drawImage(img, 305f - width /2 , y, width, rate)
        var styleRegular = template.resource().styleContentRegualar().clone().fontSize(9f)
        var styleBold    = template.resource().styleContentBold().clone().fontSize(9f)
        if(AvoidDto.Results.저위험 == dto.result) {
            stream.paragraph(
                80f, y + rate - 35, 480f, AlignHorizontal.LEFT,
                TextBlock(styleRegular, "혈액 속 암세포에서 유래된 DNA를 인공지능 알고리즘을 통해 분석한 결과\n"),
                TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, "님은 "),
                TextBlock(styleBold, "암의 존재 가능성이 낮을 것"),
                TextBlock(
                    styleRegular, "으로 예상됩니다.\n\nAVOID 검사는 모든 암을 검출할 수 없으며, 암의 병기나 종류에 따라 검출 성능이 달라질 수 있습니다.\n" +
                            "본 검사는 수검자의 암 존재 가능성을 확인하는 검사로 정확한 진단을 위한 검사는 아닙니다.\n\n" +
                            "현재 암 저위험군이라도 정기적인 건강검진과 생활습관 관리를 통해 암을 예방할 것을 권장합니다."
                )
            )
        }
        else if(AvoidDto.Results.고위험 == dto.result)
            stream.paragraph(
                80f, y+rate-15, 480f, AlignHorizontal.LEFT,
                TextBlock(styleRegular, "혈액 속 암세포에서 유래된 DNA를 인공지능 알고리즘을 통해 분석한 결과 "),
                TextBlock(styleBold, dto.patientName), TextBlock(styleRegular, "님은 "),
                TextBlock(styleBold, "암의 존재 가능성이 높을 것"), TextBlock(styleRegular, "으로 예측됩니다.\n\n"),
                TextBlock(styleBold, dto.patientName),TextBlock(styleRegular, "님의 암세포 유래 DNA 이상 패턴은 7종 암 중 "),
                TextBlock(styleBold, dto.first.name), TextBlock(styleRegular, " 환자와 가장 유사하여\n 7종 암 중 "),
                TextBlock(styleBold, dto.first.name), TextBlock(styleRegular, "일 가능성이 "),
                TextBlock(styleBold, (dto.first.score!!/10f).toInt().toString()+"0%"),
                TextBlock(styleRegular, "로 가장 높은 것으로 예측됩니다.\n 일반적으로 "),
                TextBlock(styleBold, patientInfo(dto.age!!, dto.first.name, dto.sex!!)), TextBlock(styleRegular, " 환자는 "),
                TextBlock(styleBold, (round(dto.first.asr.div(1000) * 100) / 100).toString()+"%"),
                TextBlock(styleRegular, "(10만명 중에 "),
                TextBlock(styleBold, dto.first.asr.toString()+"명"), TextBlock(styleRegular, ")의 확률로 발생하지만,\nAVOID 검사 결과 "),
                TextBlock(styleBold, dto.first.name), TextBlock(styleRegular, " 고위험군인 "), TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, "님은 "+dto.first.name+"일 확률이 약 "), TextBlock(styleBold, dto.first.ppv.toString()+"%"),
                TextBlock(styleRegular, "로 예상됩니다.\n\n만일 "),
                TextBlock(styleBold, dto.first.name), TextBlock(styleRegular, "이 아닐 경우 다음으로 의심되는 암종은 "),
                TextBlock(styleBold, dto.second.name), TextBlock(styleRegular, "입니다.\n일반적으로 "),
                TextBlock(styleBold, patientInfo(dto.age!!, dto.second.name, dto.sex!!)), TextBlock(styleRegular, "는 "),
                TextBlock(styleBold, (round(dto.second.asr.div(1000) * 100) / 100).toString()+"%"),
                TextBlock(styleRegular, "(10만명 중에 "), TextBlock(styleBold, dto.second.asr.toString()+"명"),
                TextBlock(styleRegular, ")의 확률로 발생하지만,\n AVOID 검사 결과"), TextBlock(styleBold, dto.second.name),
                TextBlock(styleRegular, " 고위험군인 "), TextBlock(styleBold, dto.patientName),
                TextBlock(styleRegular, "님은 "+dto.second.name+"일 확률이 약 "), TextBlock(styleBold, dto.second.ppv.toString()+"%"),
                TextBlock(styleRegular, "로 예상됩니다.\n\nAVOID 검사는 건강상태(양성질환, 자가면역질환 등)에 따라 위양성으로 보고될 수 있습니다.\n" +
                        "수검자의 암 존재 가능성을 확인하는 검사로 정확한 진단을 위한 검사는 아닙니다.\n" +
                        "확진을 위해서는 의료진 상담을 통한 정밀 검사를 권장합니다.")
            )
        else stream.paragraph(
            80f, y + rate - 35, 480f, AlignHorizontal.LEFT,
            TextBlock(styleRegular, "혈액 속 암세포에서 유래된 DNA를 인공지능 알고리즘을 통해 분석한 결과 "), TextBlock(styleBold, dto.patientName),
            TextBlock(styleRegular, " 님은 "), TextBlock(styleBold, "암의 존재 가능성이 높을 것"), TextBlock(styleRegular, "으로 예측됩니다.\n\n"),
            TextBlock(styleBold, dto.patientName),TextBlock(styleRegular, "님의 암세포 유래 DNA 이상 패턴은 검증된 7종 암이 아닌 "),
            TextBlock(styleBold, "기타 암"), TextBlock(styleRegular, " 환자군과 유사할 가능성이 높을 것으로 예측됩니다.\n\n"),
            TextBlock(styleRegular, "일반적으로 "), TextBlock(styleBold, patientInfo(dto.age!!, null, dto.sex!!)),
            TextBlock(styleRegular, "전체 암 환자는 "), TextBlock(styleBold, (round(dto.first.asr.div(1000) * 100) / 100).toString()+"%"),
            TextBlock(styleRegular, "(10만명 중에 "), TextBlock(styleBold, dto.first.asr.toString()+"명"),
            TextBlock(styleRegular, ")의 확률로 발생하지만,\nAVOID 검사 결과 암 고위험군인 "), TextBlock(styleBold, dto.patientName),
            TextBlock(styleRegular, "님은 암일 확률이 약 "), TextBlock(styleBold, dto.first.ppv.toString()+"%"),
            TextBlock(styleRegular, "로 예상됩니다.\n\nAVOID 검사는 건강상태(양성질환, 자가면역질환 등)에 따라 위양성으로 보고될 수 있습니다.\n" +
                    "수검자의 암 존재 가능성을 확인하는 검사로 정확한 진단을 위한 검사는 아닙니다.\n" +
                    "확진을 위해서는 의료진 상담을 통한 정밀 검사를 권장합니다.")
        )
        return stream
    }
    private fun patientInfo(age: String, cancer: String?, sex: Sex) : String{
        val ageStream: String = (age.toInt()/10*10).toString()
        val cut: String = when{
            age.substring(age.length-1, age.length).toInt() >= 5 -> "후반"
            else -> "초반"
        }
        val sexStr: String = when{
            sex == Sex.F -> "여성"
            else -> "남성"
        }

        return when{
            cancer != null -> ageStream+"대 "+cut+" "+sexStr+" "+cancer
            else -> ageStream+"대 "+cut+" "+sexStr
        }
    }
    companion object {
        private const val RESULT_IMAGE_COMMENT_RATE = 186f
        private const val RESULT_IMAGE_COMMENT_RATE_LOW = 186f*0.8f
    }
}