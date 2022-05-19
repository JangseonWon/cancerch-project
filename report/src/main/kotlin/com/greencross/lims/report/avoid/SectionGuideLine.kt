package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionGuideLine (private var y: Float = 385f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()

        var style = template!!.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(12f)
        var styleBold = template.resource().styleContentBold().clone().fontSize(9f)
        var styleRegular = template.resource().styleContentRegualar().clone().fontSize(8f)
        y -= CONTENT_TITLE_RATE+10+if(dto!!.result == AvoidDto.Results.저위험)-37 else 0

        var img = template.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(305f, y+7, 200f, AlignHorizontal.CENTER, TextBlock(style, if(dto.result == AvoidDto.Results.고위험) dto.patientName+"님의 맞춤 가이드라인" else "암 검진 가이드라인"))

        if(dto.result != AvoidDto.Results.저위험) {
            img = template.resource().imgGuideLineTable()
            width = img.width * RESULT_CONTENT_RATE / img.height
            y -= RESULT_CONTENT_RATE + 10
            val cancer = when {
                dto.first.name == "기타암종" -> "기타 암"
                else -> dto.first.name
            }
            stream.drawImage(img, 305f - width / 2, y, width, RESULT_CONTENT_RATE)
            stream.paragraph(
                305f,
                y + RESULT_CONTENT_RATE - 17,
                200f,
                AlignHorizontal.CENTER,
                TextBlock(styleBold, "$cancer 고위험군 맞춤 가이드라인은 다음과 같습니다.")
            )

            img = template.resource().imgGuideLineCancer(dto.first.name)
            width = img.width * CONTENT_CANCER_RATE / img.height
            stream.drawImage(img, 100f - width / 2, y + RESULT_CONTENT_RATE - 100, width, CONTENT_CANCER_RATE)
            style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
            stream.paragraph(
                135f,
                y + RESULT_CONTENT_RATE - 102 + CONTENT_CANCER_RATE * 0.5f,
                100f,
                AlignHorizontal.LEFT,
                TextBlock(style, dto.first.name)
            )
            stream.paragraph(
                290f, y + RESULT_CONTENT_RATE - 79 + CONTENT_CANCER_RATE * 0.5f, 100f, AlignHorizontal.CENTER,
                TextBlock(styleBold.clone().color(Color(255, 255, 255)).fontSize(10f), "정밀검사")
            )
            stream.paragraph(
                462f, y + RESULT_CONTENT_RATE - 79 + CONTENT_CANCER_RATE * 0.5f, 160f, AlignHorizontal.CENTER,
                TextBlock(styleBold.clone().color(Color(255, 255, 255)).fontSize(10f), "AVOID 검사 모니터링 권장 기간")
            )
            stream.paragraph(
                255f, y+RESULT_CONTENT_RATE-69, 200f, AlignHorizontal.LEFT,
                TextBlock(styleRegular.clone().fontSize(9.5f), template.lblGuideLineDetection(dto.first.name))
            )
            stream.paragraph(
                462f, y+RESULT_CONTENT_RATE-77, 160f, AlignHorizontal.CENTER,
                TextBlock(styleRegular.clone().fontSize(10f), template.lblGuideLineTime(dto.first.name))
            )
            stream.paragraph(
                153f, y+64, 400f, AlignHorizontal.LEFT,
                TextBlock(styleRegular.clone().fontSize(9f), template.lblGuideLineComment(dto.first.name))
            )
        } else {
            img = template.resource().imgGuideLineTotalCancer()
            width = img.width * RESULT_CONTENT_LOW_RATE / img.height
            y -= RESULT_CONTENT_LOW_RATE + 10
            stream.drawImage(img, 305f - width / 2, y, width, RESULT_CONTENT_LOW_RATE)
            styleBold = styleBold.clone().color(Color(255,255,255)).fontSize(9f)

            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-24, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "암종"))
            stream.paragraph(198f, y+ RESULT_CONTENT_LOW_RATE-24, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "대상"))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-23, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "주기"))
            stream.paragraph(440f, y+ RESULT_CONTENT_LOW_RATE-22, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "검사"))

            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-50, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "폐암"))
            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-83, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "대장암"))
            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-122, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "간암"))
            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-159, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "췌장암"))
            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-191, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "식도암"))
            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-220, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "유방암"))
            stream.paragraph(89f, y+ RESULT_CONTENT_LOW_RATE-243, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "난소암"))

            stream.paragraph(125f, y+ RESULT_CONTENT_LOW_RATE-44, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "만 54세 이상 만 74세 이하 남녀\n 폐암 발생 고위험군(30갑년*이상 흡연력)"))
            stream.paragraph(125f, y+ RESULT_CONTENT_LOW_RATE-83, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "만 50세 이상 남녀"))
            stream.paragraph(125f, y+ RESULT_CONTENT_LOW_RATE-111, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "만 40세 이상 남녀 중 간암 발생 고위험군\n"), TextBlock(styleRegular.clone().fontSize(7f), "(간경변증이나 B형 간염 바이러스 항원 또는\nC형 간염바이러스 항체 양성으로 확인된 자)"))
            stream.paragraph(125f, y+ RESULT_CONTENT_LOW_RATE-154, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "만 70세 이상 남녀\n췌장암 가족력/장기 흡연자/만성췌장염 병력"))
            stream.paragraph(125f, y+ RESULT_CONTENT_LOW_RATE-191, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "증상이 있거나, 식도암이 의심되는 자"))
            stream.paragraph(125f, y+ RESULT_CONTENT_LOW_RATE-220, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "만 40세 이상 여성"))
            stream.paragraph(197f, y+ RESULT_CONTENT_LOW_RATE-242, 200f, AlignHorizontal.CENTER, TextBlock(styleRegular, "-"))

            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-48, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "2년"))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-82, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "1년"))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-119, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "6개월"))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-159, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "1년"))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-185, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "정기적인\n검사 권장"))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-220, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "2년"))
            stream.paragraph(305f, y+ RESULT_CONTENT_LOW_RATE-241, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "-"))

            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-48, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "저선량흉부CT검사"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-78, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "분변잠혈검사 이상소견 시, 대장내시경검사\n"), TextBlock(styleRegular.clone().fontSize(6.5f), "(단, 대장내시경을 실시하기 어려운 경우 대장이중조영검사 선택적 시행"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-119, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "간초음파검사, 혈청알파태아단백검사"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-159, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "복부초음파검사, 복부CT검사"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-191, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "식도-위 내시경검사"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-220, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "유방촬영검사"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE-241, 230f, AlignHorizontal.LEFT, TextBlock(styleRegular, "CA125 수치 검사 이상소견 시 초음파검사, CT검사, MRI검사"))

        }
        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 21f
        private const val CONTENT_CANCER_RATE = 70f
        private const val RESULT_CONTENT_RATE = 199f
        private const val RESULT_CONTENT_LOW_RATE = 250f
    }
}