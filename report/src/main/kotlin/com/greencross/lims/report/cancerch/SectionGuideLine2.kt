package com.greencross.lims.report.cancerch

import com.gcgenome.lims.report.TextBlock
import com.greencross.lims.report.builder.Util_EnUS
import com.greencross.lims.report.builder.Util_EnUS.Companion.cancerToEng
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.AlignVertical
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import java.awt.Color

class SectionGuideLine2(private var y: Float = 329f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
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
            CancerchDto.Results.GENERAL -> "General Cancer Screening Guidelines"
            else -> "Personalized Guidelines"
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

                    style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(if(dto.first.name == "췌장담도암") 10f else 12f)
                    val cancer = when(cancerToEng(dto.first.name)) {
                        "Others" -> "Other cancer"
                        else -> cancerToEng(dto.first.name)
                    }
                    stream.paragraph(144f, y + rate - 92 + CONTENT_CANCER_RATE * 0.5f, 80f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(style, cancer))
                    stream.paragraph(280f, y + rate - 68 + CONTENT_CANCER_RATE * 0.5f, 100f, AlignHorizontal.CENTER,
                        TextBlock(template.resource().styleContentBold().clone().color(Color(255, 255, 255)).fontSize(9f), "Diagnostic test")
                    )
                    lblGuideLineTableHeader2(stream, y, rate, dto, template)
                    stream.paragraph(238f, y+rate-74, 200f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().fontSize(9.5f),  "Consult to a Physician"))
                    lblGuideLineTime(stream, y, rate, dto, template)

                    val content =  when(dto.first.name) {
                        "폐암" -> "For a lung cancer High Risk result, consultation with the physician for follow-ups is recommended. Although lung cancer is not confirmed through further diagnostic test, potential risk for other cancer types can not be excluded.\n" +
                                "When you have the symptoms of other cancers, PET-CT test can be considered. If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."

                        "대장암" -> "For a colon cancer High Risk result, consultation with the physician for follow-ups is recommended. Although colon cancer is not confirmed through further diagnostic test, potential risk for other cancer types can not be excluded.\n" +
                                "When you have the symptoms of other cancers, PET-CT test can be considered. If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."

                        "간암" -> "For a liver cancer High Risk result, consultation with the physician for follow-ups is recommended. Although liver cancer is not confirmed through further diagnostic test, potential risk for other cancer types can not be excluded.\n" +
                                "When you have the symptoms of other cancers, PET-CT test can be considered. If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."

                        "췌장담도암" -> "For a pancreatobiliary cancer High Risk result, consultation with the physician for follow-ups is recommended. Although pancreatobiliary cancer is not confirmed through further diagnostic test, potential risk for other cancer types can not be excluded.\n" +
                                "When you have the symptoms of other cancers, PET-CT test can be considered. If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."

                        "식도암" -> "For a esophageal cancer High Risk result, consultation with the physician for follow-ups is recommended. Although esophageal cancer is not confirmed through further diagnostic test, potential risk for other cancer types can not be excluded.\n" +
                                "When you have the symptoms of other cancers, PET-CT test can be considered. If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."

                        "유방암" -> ""

                        "난소암" -> "For a ovarian cancer High Risk result, consultation with the physician for follow-ups is recommended. Although ovarian cancer is not confirmed through further diagnostic test, potential risk for other cancer types can not be excluded.\n" +
                                "When you have the symptoms of other cancers, PET-CT test can be considered. If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."

                        else -> "For the High Risk result, consultation with the physician for diagnostic test is recommended. Although cancer is not\n" +
                                "confirmed through further test, potential risks for other cancer types can not be excluded.\n" +
                                "When you have the symptoms of other cancers, PET-CT test can be considered.\n" +
                                "If cancer is still not confirmed, undergoing ai-CANCERCH test to monitor cancer risk every three months can be considered."
                    }
                    stream.paragraph(138f, y+20, 420f, AlignHorizontal.LEFT, AlignVertical.MIDDLE, TextBlock(template.resource().styleContentRegualar().clone().fontSize(8f), content))
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
                    style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
                    stream.paragraph(140f, y+125, 100f, AlignHorizontal.CENTER, AlignVertical.MIDDLE, TextBlock(style, Util_EnUS.lblResultToWord(dto.result)))
                    lblGuideLineTableHeader2(stream, y, 0f, dto, template)
                    lblGuideLineTime(stream, y, 0f, dto, template)
                    stream.paragraph(128f, y+48, 500f, AlignHorizontal.LEFT,
                        TextBlock(template.resource().styleContentRegualar().clone().fontSize(8f), "For the Intermediate Risk result, ai-CANCERCH test after three months for follow ups is recommended.\n" +
                                "The Intermediate Risk is a case in which abnormal DNA patterns is observed but the possibility of temporal abnormality\n" +
                                "due to the health status(benign disease, autoimmune disease, etc.) cannot be excluded.\n" +
                                "Please consider undergoing the ai-CANCERCH to monitor cancer risk every three months.\n" +
                                "If you have the symptoms for specific cancers, further test through the consultation with the physician is recommended.")
                    )
                }
            }

        } else {
            img = template.resource().imgGuideLineTotalCancer()
            width = img.width * RESULT_CONTENT_LOW_RATE / img.height
            y -= RESULT_CONTENT_LOW_RATE + 10
            stream.drawImage(img, 296.5f - width / 2, y, width, RESULT_CONTENT_LOW_RATE)

            val styleBold = template.resource().styleContentBold().clone().color(Color(255,255,255)).fontSize(11f)
            stream.paragraph(59f,  y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold,"Type"))
            stream.paragraph(178f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "Target"))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "Intervals"))
            stream.paragraph(440f, y+ RESULT_CONTENT_LOW_RATE -12, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "Test"))

            var styleRegular = template.resource().styleContentRegualar().clone().color(Color(23,18,15)).fontSize(7f)
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Lung"))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -73, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Colon"))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -113, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Liver"))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Pancreatobiliary"))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -187, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Esophageal"))
            stream.paragraph(62f, y+ RESULT_CONTENT_LOW_RATE -215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Ovarian"))

            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -34, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "Individuals aged 54 to 74 with a smoking\nhistory of ≥ 30 pack-years*"))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -73, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "Individuals aged 50 or older"))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -100, 200f, AlignHorizontal.LEFT,
                TextBlock(styleRegular, "Individuals aged 40 or older and at \nhigh risk of liver cancer\n"),
                TextBlock(styleRegular.clone().fontSize(6f),"(Those who have cirrhosis or test positive for the\nhepatitis B virus antigen or the hepatitis C virus antibody)"))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -144, 200f, AlignHorizontal.LEFT,
                TextBlock(styleRegular.clone().fontSize(7f), "Individuals aged 70 or older \n"),
                TextBlock(styleRegular.clone().fontSize(6f),
                    "(Family history of pancreatobiliary cancer/ long\nterm smoker / medical history of chronic pancreatitis)"))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -182, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "Individuals who have any symptoms or\nare suspected to have esophageal cancer"))
            stream.paragraph(97f, y+ RESULT_CONTENT_LOW_RATE -215, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular, "-"))

            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Every 2 years"))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -73, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Every 1 year"))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -113, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Every 6 months"))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Every 1 year"))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -182, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Regular\ncheck-ups"))
            stream.paragraph(295f, y+ RESULT_CONTENT_LOW_RATE -215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "-"))

            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE - 38, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "Low-dose chest CT"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE - 65, 220f, AlignHorizontal.LEFT, TextBlock(styleRegular, "If there are any abnormal findings in a fecal occult blood test,\ncolonoscopy can be considered.\n"), TextBlock(styleRegular.clone().fontSize(5.5f), "(If a colonoscopy is not available, a double contrast brium enema may be an option.)"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -113, 200f, AlignHorizontal.LEFT, TextBlock(styleRegular,"Liver ultrasound, AFP test"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -152, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "Abdominal ultrasound, CT"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -186, 100f, AlignHorizontal.LEFT, TextBlock(styleRegular, "Esophageal-Gastric endoscopy"))
            stream.paragraph(343f, y+ RESULT_CONTENT_LOW_RATE -211, 230f, AlignHorizontal.LEFT, TextBlock(styleRegular, "If there are any abnormal findings in a CA125 test, ultrasound,\nCT or MRI can be considered."))

            styleRegular = template.resource().styleContentRegualar().clone().fontSize(7f)

            stream.paragraph(400f, y+ RESULT_CONTENT_LOW_RATE -232, 230f, AlignHorizontal.LEFT, TextBlock(styleRegular.clone().color(Color(55,55,55)).fontSize(6f), "*Pack-years: Average daily smoking amount (packs)"))
        }
        return stream
    }
    private fun lblGuideLineTableHeader2(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float, dto: CancerchDto, template: CancerchTemplate<CancerchResource>?) {
        when(dto.result) {
            CancerchDto.Results.RISK -> stream.paragraph(468f, y + rate - 68 + CONTENT_CANCER_RATE * 0.5f, 160f, AlignHorizontal.CENTER,
                TextBlock(template!!.resource().styleContentBold().clone().color(Color(255, 255, 255)).fontSize(9f), "ai-CANCERCH Test")
            )
            else -> stream.paragraph(380f, y + RESULT_CONTENT_OTH_RATE - 129 + RESULT_CONTENT_OTH_RATE * 0.5f, 160f, AlignHorizontal.CENTER,
                TextBlock(template!!.resource().styleContentBold().clone().color(Color(255, 255, 255)).fontSize(9f), "ai-CANCERCH Test")
            )
        }
    }
    private fun lblGuideLineTop(stream: PDPageContentStreamPageAccessible, y: Float, dto: CancerchDto, template: CancerchTemplate<CancerchResource>?) {
        val styleBold = template!!.resource().styleContentBold().clone().fontSize(11f)
        when (dto.result) {
            CancerchDto.Results.CONCERN -> stream.paragraph(305f, y, 400f, AlignHorizontal.CENTER, TextBlock(styleBold, "Intermediate Risk Guidelines for ${dto.patientName}"))
            else -> {
                val cancer = when(cancerToEng(dto.first.name)) {
                    "Others" -> "Other cancer"
                    else -> cancerToEng(dto.first.name)
                }
                stream.paragraph(305f, y, 400f, AlignHorizontal.CENTER, TextBlock(styleBold, "$cancer Guidelines for ${dto.patientName}."))
            }
        }
    }
    private fun lblGuideLineTime(stream: PDPageContentStreamPageAccessible, y: Float, rate: Float, dto: CancerchDto, template: CancerchTemplate<CancerchResource>?) {
        val time = when (dto.first.name) {
            "폐암" -> "Retest After 3 months"
            "대장암" -> "Retest After 3 months"
            "간암" -> "Retest After 3 months"
            "췌장담도암" -> "Retest After 3 months"
            "식도암" -> "Retest After 3 months"
            "유방암" -> "Retest After 3 months"
            "난소암" -> "Retest After 3 months"
            else -> "Retest After 3 months"
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
