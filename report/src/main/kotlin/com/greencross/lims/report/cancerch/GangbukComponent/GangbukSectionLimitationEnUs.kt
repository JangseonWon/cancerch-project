package com.greencross.lims.report.cancerch.GangbukComponent

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.cancerch.CancerchResource
import com.greencross.lims.report.cancerch.CancerchTemplate
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.AlignVertical
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class GangbukSectionLimitationEnUs(private var y: Float = 755f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        var style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f)
        var styleBold = template.resource().styleContentBold().clone().fontSize(11f)
        var styleRegular = template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f)
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(297f, y+10, 200f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "Disclaimers"))

        y -= 10
        for(i in 0..6){
            y -= CONTENT_MINI_SQUARE +10
            img = template.resource().imgMiniSquare()
            width = img.width * CONTENT_MINI_SQUARE / img.height

            when (i) {
                0 -> {
                    stream.drawImage(img, 50f-width/2, y+1, width, CONTENT_MINI_SQUARE)
                    stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), "This test screens for cancer by analyzing patterns in cfDNA, and a cancer signal does not indicate a diagnosis of cancer."))
                }
                1 -> {
                    stream.drawImage(img, 50f-width/2, y+1, width, CONTENT_MINI_SQUARE)
                    stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), "This test cannot detect all types of cancer and the test performance may differ depending on the stage or type of cancer."))
                }
                2 -> {
                    stream.drawImage(img, 50f-width/2, y+1, width, CONTENT_MINI_SQUARE)
                    stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), "This test is developed using Lung, Colon, Liver, Pancreatobiliary, Esophageal, and Ovarian cancer sample data. Other cancer types cannot be analyzed accurately."))
                }
                3 -> {
                    stream.drawImage(img, 50f-width/2, y+1, width, CONTENT_MINI_SQUARE)
                    stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), "The sensitivity may vary depending on the location and genetic characteristics of the cancer."))
                }
                4 -> {
                    stream.drawImage(img, 50f-width/2, y+1, width, CONTENT_MINI_SQUARE)
                    stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), "The test performance and tested cancer type can be modified according to the ML-algorithm improvement."))
                }
                5 -> {
                    stream.drawImage(img, 50f-width/2, y+1, width, CONTENT_MINI_SQUARE)
                    stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, AlignVertical.TOP, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), "This test may be reported as false positive in the examinee with benign diseases, autoimmune diseases, etc., and may be reported as false negative in case of\n" +
                            "having chemotherapy, cell therapy, etc."))
                }
                6 -> {
                    stream.drawImage(img, 50f-width/2, y- CONTENT_MINI_SQUARE -5, width, CONTENT_MINI_SQUARE)
                    stream.paragraph(56f, y- CONTENT_MINI_SQUARE -5, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0, 0, 0)).fontSize(7f), "This test cannot replace the existing health check-up test."))
                }
                else -> stream.paragraph(56f, y, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f), ""))
            }
        }

        y -= CONTENT_TABLE_TITLE +20
        img = template.resource().imgLimitationTableTitle()
        width = img.width * CONTENT_TABLE_TITLE / img.height
        stream.drawImage(img, 123f-width/2, y, width, CONTENT_TABLE_TITLE)
        stream.paragraph(123f, y+6, 300f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(Color(67,72,142)).fontSize(9f), "ai-CANCERCH Performance"))

        y -= CONTENT_TABLE +10
        img = template.resource().imgLimitationTable1()
        width = img.width * CONTENT_TABLE / img.height
        styleRegular = styleRegular.clone().color(Color(255,255,255)).fontSize(8f)
        styleBold = styleBold.clone().color(Color(255,255,255)).fontSize(9f)
        val styleRegularInner = styleRegular.clone().color(Color(81,81,81)).fontSize(8f)
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TABLE)
        stream.paragraph( 88f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Cancer Type"))
        stream.paragraph(188f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Specificity¹⁾"))
        stream.paragraph(293f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Sensitivity²⁾"))
        stream.paragraph(392f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "PPV³⁾"))
        stream.paragraph(498f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "NPV⁴⁾"))

        stream.paragraph( 88f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Overall"))
        stream.paragraph(188f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto!!.result != CancerchDto.Results.RISK) "95.0%" else "99.0%"))
        stream.paragraph(293f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "81.1%" else "60.1%"))
        stream.paragraph(392f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "67.1%" else "88.3%"))
        stream.paragraph(498f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "97.6%" else "95.2%"))

        stream.paragraph( 88f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Lung"))
        stream.paragraph(188f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "95.0%" else "99.0%"))
        stream.paragraph(293f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "73.1%" else "46.2%"))
        stream.paragraph(392f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "9.2%"  else "24.3%"))
        stream.paragraph(498f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) ">98%"  else ">98%"))

        stream.paragraph( 88f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Colon"))
        stream.paragraph(188f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "95.0%"  else "99.0%"))
        stream.paragraph(293f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "70.1%"  else "45.3%"))
        stream.paragraph(392f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "13.2%"  else "32.9%"))
        stream.paragraph(498f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) ">98%"   else ">98%"))

        stream.paragraph( 88f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Liver"))
        stream.paragraph(188f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "95.0%"  else  "99.0%"))
        stream.paragraph(293f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "94.5%"  else  "82.4%"))
        stream.paragraph(392f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "7.1%"   else  "24.9%"))
        stream.paragraph(498f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) ">98%"   else  ">98%"))

        stream.paragraph( 88f,  y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Pancreatobiliary"))
        stream.paragraph(188f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "95.0%"  else  "99.0%"))
        stream.paragraph(293f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "91.3%"  else  "76.1%"))
        stream.paragraph(392f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "4.9%"   else  "17.5%"))
        stream.paragraph(498f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) ">98%"   else  ">98%"))

        stream.paragraph( 88f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Esophageal"))
        stream.paragraph(188f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "95.0%"  else  "99.0%"))
        stream.paragraph(293f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "88.4%"  else  "58.1%"))
        stream.paragraph(392f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "1.4%"   else  "4.4%"))
        stream.paragraph(498f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) ">98%"   else  ">98%"))

        stream.paragraph( 88f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Ovarian"))
        stream.paragraph(188f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "95.0%"  else  "99.0%"))
        stream.paragraph(293f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "70.4%"  else  "51.9%"))
        stream.paragraph(392f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "1.2%"   else  "4.3%"))
        stream.paragraph(498f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) ">98%"   else  ">98%"))

        y -= 4
        for(row in 0..3){
            y -= 8
            when (row) {
                0 -> stream.paragraph(42f, y, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5f), "1) Specificity: Indicates the proportion where the ai-CANCERCH test classifies a healthy individual to the low risk group."))
                1 -> stream.paragraph(42f, y, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5f), "2) Sensitivity: Indicates the proportion where the ai-CANCERCH test classifies a cancer patient to high or intermediate risk group."))
                2 -> stream.paragraph(42f, y, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5f), "3) PPV: Positive Predictive Value. Represents the proportion of subjects identified by the ai-CANCERCH test as part of the high or intermediate risk group who are actual cancer patients. PPV has been calculated based on the\n" +
                        "   prevalence of the 50s and above Korean."))
                3 -> stream.paragraph(42f, y-6, 600f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5f), "4) NPV: Negative Predictive Value. Represents the proportion of subjects identified by the ai-CANCERCH test as part of the low risk group who are healthy individuals. NPV has been calculated based on the prevalence of the 50s and\n" +
                        "   above Korean."))
                else -> stream.paragraph(42f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5f), ""))
            }
        }

        y -= CONTENT_TABLE_TITLE +20
        img = template.resource().imgLimitationTableTitle()
        styleRegular = styleRegular.clone().color(Color(255,255,255)).fontSize(8f)
        width = img.width * CONTENT_TABLE_TITLE / img.height
        stream.drawImage(img, 123f-width/2, y, width, CONTENT_TABLE_TITLE)
        stream.paragraph(123f, y+6, 300f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(Color(67,72,142)).fontSize(9f), "Other Screening Test Performance"))

        y -= CONTENT_TABLE2 + 7
        img = template.resource().imgLimitationTable2()
        width = img.width * CONTENT_TABLE2 / img.height

        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TABLE2)
        stream.paragraph(88f,  y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Cancer Type"))
        stream.paragraph(205f, y+118, 150f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Method"))
        stream.paragraph(316f, y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Specificity"))
        stream.paragraph(383f, y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "Sensitivity"))
        stream.paragraph(452f, y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "PPV"))
        stream.paragraph(519f, y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "NPV"))

        stream.paragraph(88f,  y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Lung"))
        stream.paragraph(205f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "Low-dose Chest CT"))
        stream.paragraph(316f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 92.6%"))
        stream.paragraph(383f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 88.9%"))
        stream.paragraph(452f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 6.3%"))
        stream.paragraph(519f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        stream.paragraph(88f,  y+77, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Colon"))
        stream.paragraph(205f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "Stool Occult Blood Test"))
        stream.paragraph(316f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "95.4%"))
        stream.paragraph(383f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 40.0%"))
        stream.paragraph(452f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 7.5%"))
        stream.paragraph(519f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        stream.paragraph(205f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "Colonoscopy"))
        stream.paragraph(316f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 99.0%"))
        stream.paragraph(383f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 85.0~95.0%"))
        stream.paragraph(452f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 44.1~46.9%"))
        stream.paragraph(519f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        stream.paragraph(88f,  y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Liver"))
        stream.paragraph(205f, y+54, 150f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "Liver ultrasound & AFP test"))
        stream.paragraph(316f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 94.0%"))
        stream.paragraph(383f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 80.0%"))
        stream.paragraph(452f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 4.5%"))
        stream.paragraph(519f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        stream.paragraph(88f,  y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Pancreatobiliary"))
        stream.paragraph(205f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "CA19-9 Test"))
        stream.paragraph(316f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 79.9~85.3%"))
        stream.paragraph(383f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 76.1~80.2%"))
        stream.paragraph(452f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 0.3~0.4%"))
        stream.paragraph(519f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        stream.paragraph(88f,  y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Esophageal"))
        stream.paragraph(205f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "Endoscopy"))
        stream.paragraph(316f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 79.0%"))
        stream.paragraph(383f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 62.0%"))
        stream.paragraph(452f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 0.2%"))
        stream.paragraph(519f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        stream.paragraph(88f,  y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "Ovarian"))
        stream.paragraph(205f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "CA125 Test"))
        stream.paragraph(316f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 95.0%"))
        stream.paragraph(383f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 43.3%"))
        stream.paragraph(452f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "~ 0.6%"))
        stream.paragraph(519f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        y -= CONTENT_TITLE_RATE +10
        img = template.resource().imgContentTitle()
        width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(297f, y+10, 200f, AlignHorizontal.CENTER, TextBlock(style, "References"))

        y -= 50
        stream.line(33f, y+42, 560f, y+42).setLineWidth(0.3f).setStrokingColor(Color(67,72,142)).stroke()
        stream.line(33f, y-36, 560f, y-36).setLineWidth(0.3f).setStrokingColor(Color(67,72,142)).stroke()
        stream.line(293f, y+32, 293f, y-26).setLineWidth(0.3f).setLineDashPattern(floatArrayOf(1f, 1.5f), 1f).setStrokingColor(Color.GRAY).stroke()
        styleRegular = styleRegular.clone().color(Color(81,81,81)).fontSize(6.5f)

        stream.paragraph(57f, y+22, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular, "1.\tCancer Biol Ther. 2019; 20(8): 1057–1067.\n" +
                "2.\tMutat Res. Jul-Sep 2019;781:100-129.\n" +
                "3.\tBMC Cancer. 2017; 17: 697.\n" +
                "4.\tCancer Resaerch. 2022:82(12, Supplement):6371-6371\n" +
                "5.\tBr J Cancer. 2008;98(10):1602-7.\n" +
                "6.\tJournal of Korean Society of Gastrointestinal Endoscopy,\n" +
                "\t2007;35(2): 68-73."))

        stream.paragraph(312f, y+22, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular, "7.\tJAMA . 2016;315(23):2564-2575.\n" +
                "8.\tAliment Pharmacol Ther. 2009;30(1):37-47.\n" +
                "9.\tOnco Targets Ther. 2016;9:7459-7467.\n" +
                "10.\tCurr Mol Med. 2013;13(3):340-51.\n" +
                "11.\tWorld J Gastroenterol. 2015;21(26):7933-43.\n" +
                "12.\tGynecol Oncol. 2008;108(2):402-8."))

        y -= 40

        y -= CONTENT_MINI_SQUARE +10
        img = template.resource().imgMiniSquare()
        width = img.width * CONTENT_MINI_SQUARE *2 / img.height
        stream.drawImage(img, 50f-width/2, y, width, CONTENT_MINI_SQUARE *2)
        stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(styleBold.clone().color(Color(67,72,142)).fontSize(8f), "Clinical Significance of Genes"))
        stream.paragraph(56f, y-10f, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular.fontSize(6.5f), "This test has not established the clinical significance of its results, and there is still insufficient evidence for the utility of health-related actions based on it.\n" +
                "- circulating tumor DNA test for Lung, Colon, Liver, Pancreas, Esophageal, and Ovarian cancer."))

        stream.paragraph(297f, y-40, 600f, AlignHorizontal.CENTER,
            TextBlock(template.resource().styleContentRegualar().clone().color(Color(151,151,151)).fontSize(6.5f), "※This test was developed and its performance characteristics determined by GC Genome."),
        )
        return stream
    }

    companion object {
        private const val CONTENT_TITLE_RATE = 28f
        private const val CONTENT_TABLE_TITLE = 18f
        private const val CONTENT_MINI_SQUARE = 2.5f
        private const val CONTENT_TABLE = 128f
        private const val CONTENT_TABLE2 = 128f
    }
}
