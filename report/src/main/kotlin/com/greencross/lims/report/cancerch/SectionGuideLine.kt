package com.greencross.lims.report.cancerch

import com.gcgenome.lims.report.TextBlock
import com.greencross.lims.report.builder.Util
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.AlignVertical
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.awt.Color

class SectionGuideLine(private var y: Float = 349f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        y -= CONTENT_TITLE_RATE + 10 + if(dto!!.result == CancerchDto.Results.GENERAL) -35 else 0

        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TITLE_RATE)
        var style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
        val title = when(dto.result) {
            CancerchDto.Results.GENERAL ->
                "암 검진 가이드라인"
            else -> dto.patientName + "님의 맞춤 가이드라인"
        }
        stream.paragraph(297f, y+10, 300f, AlignHorizontal.CENTER, TextBlock(style, title))

        if(dto.result != CancerchDto.Results.GENERAL) {
            y -= 8
            stream.line(34f, y, 561f, y).setStrokingColor(Color(55,55,55)).setLineWidth(0.2f).stroke()
            when(dto.result){
                CancerchDto.Results.RISK -> {
                    img = template.resource().imgGuideLineTable(if(dto.first.name == "기타암종") "OTHERS" else dto.result.name)
                    val rate = RESULT_CONTENT_RATE
                    width = img.width * rate / img.height
                    y -= rate + 10

                    stream.drawImage(img, 297f - width / 2, y-24, width, rate)
                    lblGuideLineTop(stream, y + rate - 9, dto, template)

                    img = template.resource().imgGuideLineCancer(dto.first.name)
                    width = img.width * CONTENT_CANCER_RATE / img.height
                    stream.drawImage(img, 75f - width / 2, y + rate - 90, width, CONTENT_CANCER_RATE)

                    style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(if(dto.first.name == "췌장담도암") 12f else 14f)
                    stream.paragraph(148f, y + rate - 97 + CONTENT_CANCER_RATE * 0.5f, 100f, AlignHorizontal.CENTER,
                        TextBlock(style, if(dto.first.name == "기타암종") "기타 암" else dto.first.name)
                    )
                    stream.paragraph(280f, y + rate - 68 + CONTENT_CANCER_RATE * 0.5f, 100f, AlignHorizontal.CENTER,
                        TextBlock(template.resource().styleContentBold().clone().color(Color(255, 255, 255)).fontSize(10f), "정밀검사")
                    )
                    lblGuideLineTableHeader2(stream, y, rate, dto, template)
                    stream.paragraph(240f, y+rate-74, 200f, AlignHorizontal.LEFT,
                        TextBlock(template.resource().styleContentRegualar().clone().fontSize(9.5f),  "주치의와 상담요함")
                    )
                    lblGuideLineTime(stream, y, rate, dto, template)

                    val content =  when(dto.first.name) {
                        "폐암" -> "아이캔서치 검사 폐암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                                "정밀검사를 통해 폐암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                                "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                                "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

                        "대장암" -> "아이캔서치 검사 대장암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                                "정밀검사를 통해 대장암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                                "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                                "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

                        "간암" -> "아이캔서치 검사 간암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                                "정밀검사를 통해 간암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                                "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                                "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

                        "췌장담도암" -> "아이캔서치 검사 췌장담도암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                                "정밀검사를 통해 췌장담도암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                                "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                                "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

                        "식도암" -> "아이캔서치 검사 식도암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                                "정밀검사를 통해 식도암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                                "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                                "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

                        "유방암" -> "아이캔서치 검사 유방암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                                "정밀검사를 통해 유방암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                                "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                                "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

                        "난소암" -> "아이캔서치 검사 난소암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                                "정밀검사를 통해 난소암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                                "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                                "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."

                        else -> "아이캔서치 검사 기타 암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                                "정밀검사를 통해 암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                                "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                                "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
                    }
                    stream.paragraph(144f, y+34, 400f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().fontSize(9f), content))
                }
                else -> {
                    img = template.resource().imgGuideLineTable(CancerchDto.Results.CONCERN.name)
                    width = img.width * RESULT_CONTENT_OTH_RATE / img.height
                    y -= RESULT_CONTENT_OTH_RATE + 10

                    stream.drawImage(img, 297f - width / 2, y-24, width, RESULT_CONTENT_OTH_RATE)
                    lblGuideLineTop(stream, y + RESULT_CONTENT_OTH_RATE - 9, dto, template)

                    img = template.resource().imgGuideLineCancer(dto.first.name)
                    width = img.width * CONTENT_CANCER_RATE / img.height
                    stream.drawImage(img, 75f - width / 2, y + CONTENT_CANCER_RATE+30, width, CONTENT_CANCER_RATE)
                    val style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
                    stream.paragraph(140f, y+125, 100f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(style, Util.lblResultToWord(dto.result)))
                    lblGuideLineTableHeader2(stream, y, 0f, dto, template)
                    lblGuideLineTime(stream, y, 0f, dto, template)

                    stream.paragraph(134f, y+50, 500f, AlignHorizontal.LEFT,
                        TextBlock(template.resource().styleContentRegualar().clone().fontSize(9f), "아이캔서치 검사 관심관리군은 3개월 후 본 검사를 통해 암 DNA를 추적할 것을 권장합니다.\n" +
                                "관심관리군은 암환자와 다소 유사한 DNA 이상 패턴이 관찰되었으나,\n" +
                                "건강상태(양성질환, 자가면역질환 등)에 따른 일시적인 이상 패턴 검출의 가능성을 배제할 수 없는 경우입니다.\n" +
                                "3개월 주기로 본 검사를 통해 암 DNA에 의한 이상 패턴을 추적할 것을 권장합니다.\n" +
                                "증상 등이 동반되어 특정 암종이 의심될 경우 의료진 상담을 통한 해당 암종에 대한 정밀 검사를 권장합니다.")
                    )
                }
            }

        } else {
            img = template.resource().imgGuideLineTotalCancer()
            width = img.width * RESULT_CONTENT_LOW_RATE / img.height
            y -= RESULT_CONTENT_LOW_RATE + 10
            stream.drawImage(img, 296.5f - width / 2, y, width, RESULT_CONTENT_LOW_RATE)

            val styleBold = template.resource().styleContentBold().clone().color(Color(255,255,255)).fontSize(9f)
            stream.paragraph(59f,  y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold,"암종"))
            stream.paragraph(178f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "대상"))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "주기"))
            stream.paragraph(440f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "검사"))

            val styleRegular = template.resource().styleContentRegualar().clone().fontSize(8f)
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "폐암"))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -73, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "대장암"))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -113, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "간암"))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "췌장담도암"))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -187, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "식도암"))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "난소암"))

            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -34, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "만 54세 이상 만 74세 이하 남녀\n 폐암 발생 고위험군(30갑년*이상 흡연력)"))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -73, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "만 50세 이상 남녀"))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -102, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "만 40세 이상 남녀 중 간암 발생 고위험군\n"), TextBlock(styleRegular.clone().fontSize(7f), "(간경변증이나 B형 간염 바이러스 항원 또는\nC형 간염 바이러스 항체 양성으로 확인된 자)"))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -147, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().fontSize(7f), "만 70세 이상 남녀\n췌장담도암 가족력/장기 흡연자/만성췌장염 병력"))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -187, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "증상이 있거나, 식도암이 의심되는 자"))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -215, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "-"))

            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "2년"))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -73, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "1년"))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -113, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "6개월"))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "1년"))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -182, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "정기적인\n검사 권장"))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "-"))

            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "저선량흉부CT검사"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -69, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "분변잠혈검사 이상소견 시, 대장내시경검사\n"), TextBlock(styleRegular.clone().fontSize(6.5f), "(단, 대장내시경을 실시하기 어려운 경우 대장이중조영검사 선택적 시행)"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -113, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular,"간초음파검사, 혈청알파태아단백검사"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "복부초음파검사, 복부CT검사"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -187, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "식도-위 내시경검사"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -215, 230f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().fontSize(7.5f), "CA125 암표지자 검사 이상소견 시 초음파검사, CT검사, MRI검사"))

            stream.paragraph(400f, y+ RESULT_CONTENT_LOW_RATE -232, 230f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().clone().color(Color(55,55,55)).fontSize(5f), "*갑년 : 일평균 흡연량(갑) x 흡연기간(년) ex) 2갑 x 15년 = 30갑년(검진 대상)"))
        }
        return stream
    }

    private fun lblGuideLineTableHeader2(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float, dto: CancerchDto, template: CancerchTemplate<CancerchResource>?) {
        when(dto.result) {
            CancerchDto.Results.RISK -> stream.paragraph(468f, y + rate - 68 + CONTENT_CANCER_RATE * 0.5f, 160f, AlignHorizontal.CENTER,
                TextBlock(template!!.resource().styleContentBold().clone().color(Color(255, 255, 255)).fontSize(10f), "아이캔서치 검사 모니터링 권장 기간")
            )
            else -> stream.paragraph(380f, y + RESULT_CONTENT_OTH_RATE - 129 + RESULT_CONTENT_OTH_RATE * 0.5f, 160f, AlignHorizontal.CENTER,
                TextBlock(template!!.resource().styleContentBold().clone().color(Color(255, 255, 255)).fontSize(10f), "아이캔서치 검사 모니터링 권장 기간")
            )
        }
    }

    private fun lblGuideLineTop(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto, template: CancerchTemplate<CancerchResource>?) {
        val styleBold = template!!.resource().styleContentBold().clone().fontSize(11f)
        when (dto.result) {
            CancerchDto.Results.CONCERN -> stream.paragraph(305f, y, 400f, AlignHorizontal.CENTER, TextBlock(styleBold, "관심관리군 맞춤 가이드라인은 다음과 같습니다"))
            else -> {
                val cancer = when {
                    dto.first.name == "기타암종" -> "기타 암"
                    else -> dto.first.name
                }
                stream.paragraph(305f, y, 400f, AlignHorizontal.CENTER, TextBlock(styleBold, "$cancer 집중관리군 맞춤 가이드라인은 다음과 같습니다."))
            }
        }
    }

    private fun lblGuideLineTime(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float, dto: CancerchDto, template: CancerchTemplate<CancerchResource>?) {
        val RESULT_CONTENT_OTH_RATE = 182f
        val time = when (dto.first.name) {
            "폐암" -> "3개월 후"
            "대장암" -> "3개월 후"
            "간암" -> "3개월 후"
            "췌장담도암" -> "3개월 후"
            "식도암" -> "3개월 후"
            "유방암" -> "3개월 후"
            "난소암" -> "3개월 후"
            else -> "3개월 후"
        }
        when (dto.result) {
            CancerchDto.Results.RISK -> stream.paragraph(468f, y+rate-74, 160f, AlignHorizontal.CENTER, TextBlock(template!!.resource().styleContentRegualar().clone().fontSize(10f), time))
            else -> stream.paragraph(380f, y+ RESULT_CONTENT_OTH_RATE -75, 160f, AlignHorizontal.CENTER, TextBlock(template!!.resource().styleContentRegualar().clone().fontSize(10f), time))
        }

    }

    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val CONTENT_CANCER_RATE = 60f
        private const val RESULT_CONTENT_RATE = 164.5f
        private const val RESULT_CONTENT_OTH_RATE = 182f
        private const val RESULT_CONTENT_LOW_RATE = 225f
    }
}
