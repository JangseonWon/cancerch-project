package com.greencross.lims.report.cancerch.GangbukComponent

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.cancerch.CancerchResource
import com.greencross.lims.report.cancerch.CancerchTemplate
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class GangbukSectionLimitationKoKr(private var y: Float = 755f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
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
        stream.paragraph(297f, y+10, 200f, AlignHorizontal.CENTER, TextBlock(template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(14f), "검 사 한 계"))

        y -= 10
        for(row in 0..6){
            y -= CONTENT_MINI_SQUARE +10
            img = template.resource().imgMiniSquare()
            width = img.width * CONTENT_MINI_SQUARE / img.height
            stream.drawImage(img, 50f-width/2, y, width, CONTENT_MINI_SQUARE)
            when (row) {
                0 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "본 검사는 암세포 유래 cfDNA 특성 분석을 통해 암의 존재 가능성을 예측하는 검사로, 확진 목적으로 사용할 수 없습니다."))
                1 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "본 검사는 모든 암을 검출할 수 없으며, 암의 병기나 종류에 따라 검출 성적이 달라질 수 있습니다."))
                2 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "본 검사의 데이터는 주요 암종인 폐암, 대장암, 간암, 췌장담도암, 식도암, 난소암을 포함하고 있으며 기타 암종은 정확한 분석이 어렵습니다."))
                3 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "암종의 위치 및 유전적 특성에 따라 검출민감도가 상이할 수 있습니다."))
                4 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "본 검사는 내부적으로 축적된 데이터에 따라 검사 대상 암종 확대 및 성능이 변경될 수 있습니다."))
                5 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "본 검사는 양성질환, 자가면역질환 등에서 위양성으로 보고될 수 있으며, 항암치료, 세포치료 등에 따라서 위음성으로 보고될 수 있습니다."))
                6 -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), "본 검사는 기존의 건강검진 검사를 대체할 수는 없습니다."))
                else -> stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(8f), ""))
            }
        }

        y -= CONTENT_TABLE_TITLE +15
        img = template.resource().imgLimitationTableTitle()
        width = img.width * CONTENT_TABLE_TITLE / img.height
        stream.drawImage(img, 123f-width/2, y, width, CONTENT_TABLE_TITLE)
        stream.paragraph(123f, y+6, 300f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(Color(67,72,142)).fontSize(9f), "종양 DNA 혈액검사의 암종별 성능"))

        y -= CONTENT_TABLE +5
        img = template.resource().imgLimitationTable1()
        width = img.width * CONTENT_TABLE / img.height
        styleRegular = styleRegular.clone().color(Color(255,255,255)).fontSize(8f)
        styleBold = styleBold.clone().color(Color(255,255,255)).fontSize(9f)
        var styleRegularInner = styleRegular.clone().color(Color(81,81,81)).fontSize(8f)
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TABLE)
        stream.paragraph( 88f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "암종"))
        stream.paragraph(188f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "특이도¹⁾"))
        stream.paragraph(293f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "민감도²⁾"))
        stream.paragraph(392f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "양성예측도³⁾"))
        stream.paragraph(498f, y+117, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "음성예측도⁴⁾"))

        stream.paragraph( 88f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "전체"))
        stream.paragraph(188f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto!!.result != CancerchDto.Results.RISK) "95.0%" else "99.0%"))
        stream.paragraph(293f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "81.1%" else "60.1%"))
        stream.paragraph(392f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "67.1%" else "88.3%"))
        stream.paragraph(498f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "97.6%" else "95.2%"))

        stream.paragraph( 88f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "폐암"))
        stream.paragraph(188f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "95.0%" else "99.0%"))
        stream.paragraph(293f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "73.1%" else "46.2%"))
        stream.paragraph(392f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "9.2%"  else "24.3%"))
        stream.paragraph(498f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) ">98%"  else ">98%"))

        stream.paragraph( 88f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "대장암"))
        stream.paragraph(188f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "95.0%"  else "99.0%"))
        stream.paragraph(293f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "70.1%"  else "45.3%"))
        stream.paragraph(392f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "13.2%"  else "32.9%"))
        stream.paragraph(498f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) ">98%"   else ">98%"))

        stream.paragraph( 88f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "간암"))
        stream.paragraph(188f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "95.0%"  else  "99.0%"))
        stream.paragraph(293f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "94.5%"  else  "82.4%"))
        stream.paragraph(392f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "7.1%"   else  "24.9%"))
        stream.paragraph(498f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) ">98%"   else  ">98%"))

        stream.paragraph(88f,  y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "췌장담도암"))
        stream.paragraph(188f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "95.0%"  else  "99.0%"))
        stream.paragraph(293f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "91.3%"  else  "76.1%"))
        stream.paragraph(392f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "4.9%"   else  "17.5%"))
        stream.paragraph(498f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) ">98%"   else  ">98%"))

        stream.paragraph(88f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,       "식도암"))
        stream.paragraph(188f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "95.0%"  else  "99.0%"))
        stream.paragraph(293f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "88.4%"  else  "58.1%"))
        stream.paragraph(392f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "1.4%"   else  "4.4%"))
        stream.paragraph(498f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) ">98%"   else  ">98%"))

        stream.paragraph(88f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,       "난소암"))
        stream.paragraph(188f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "95.0%"  else  "99.0%"))
        stream.paragraph(293f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "70.4%"  else  "51.9%"))
        stream.paragraph(392f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) "1.2%"   else  "4.3%"))
        stream.paragraph(498f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, if(dto.result != CancerchDto.Results.RISK) ">98%"   else  ">98%"))

        y -= 4
        for(row in 0..3){
            y -= 8
            when (row) {
                0 -> stream.paragraph(42f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5.5f), "1) 특이도 : 건강인을 검사했을 때 종양 DNA 혈액검사가 일반관리군으로 판단한 비율을 의미합니다."))
                1 -> stream.paragraph(42f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5.5f), "2) 민감도 : 암환자를 검사했을 때 종양 DNA 혈액검사가 관심관리군 · 집중관리군으로 판단한 비율을 의미합니다."))
                2 -> stream.paragraph(42f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5.5f), "3) 양성예측도 : 종양 DNA 혈액검사에서 관심관리 · 집중관리로 판단한 수검자가 실제 암환자일 비율을 의미합니다. 50대 이상의 유병률에 기초하여 양성예측도가 계산되었습니다."))
                3 -> stream.paragraph(42f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5.5f), "4) 음성예측도 : 종양 DNA 혈액검사에서 일반관리로 판단한 수검자가 실제 건강인일 비율을 의미합니다. 50대 이상의 유병률에 기초하여 음성예측도가 계산되었습니다."))
                else -> stream.paragraph(42f, y, 500f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(121,121,121)).fontSize(5.5f), ""))
            }
        }

        y -= CONTENT_TABLE_TITLE +10
        img = template.resource().imgLimitationTableTitle()
        styleRegular = styleRegular.clone().color(Color(255,255,255)).fontSize(8f)
        width = img.width * CONTENT_TABLE_TITLE / img.height
        stream.drawImage(img, 123f-width/2, y, width, CONTENT_TABLE_TITLE)
        stream.paragraph(123f, y+6, 300f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(Color(67,72,142)).fontSize(9f), "기존 선별검사 성능"))

        y -= CONTENT_TABLE2 + 5
        img = template.resource().imgLimitationTable2()
        width = img.width * CONTENT_TABLE2 / img.height
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TABLE2)
        stream.paragraph(88f,  y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "암종"))
        stream.paragraph(205f, y+118, 150f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "검사"))
        stream.paragraph(316f, y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "특이도"))
        stream.paragraph(383f, y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "민감도"))
        stream.paragraph(452f, y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "양성예측도"))
        stream.paragraph(519f, y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "음성예측도"))

        stream.paragraph(88f,  y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "폐암"))
        stream.paragraph(205f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "저선량흉부CT검사"))
        stream.paragraph(316f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 92.6%"))
        stream.paragraph(383f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 88.9%"))
        stream.paragraph(452f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 6.3%"))
        stream.paragraph(519f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        stream.paragraph(88f,  y+77, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,       "대장암"))
        stream.paragraph(205f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner,  "분변잠혈검사"))
        stream.paragraph(316f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner,  "95.4%"))
        stream.paragraph(383f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner,  "약 40.0%"))
        stream.paragraph(452f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner,  "약 7.5%"))
        stream.paragraph(519f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner,  ">98%"))

        stream.paragraph(205f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "대장내시경 검사"))
        stream.paragraph(316f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 99.0%"))
        stream.paragraph(383f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 85.0~95.0%"))
        stream.paragraph(452f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 44.1~46.9%"))
        stream.paragraph(519f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        stream.paragraph(88f,  y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "간암"))
        stream.paragraph(205f, y+54, 150f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "간초음파 및 혈청알파태아단백검사"))
        stream.paragraph(316f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 94.0%"))
        stream.paragraph(383f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 80.0%"))
        stream.paragraph(452f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 4.5%"))
        stream.paragraph(519f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        stream.paragraph(88f,  y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "췌장담도암"))
        stream.paragraph(205f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "CA19-9 암표지자 검사"))
        stream.paragraph(316f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 79.9~85.3%"))
        stream.paragraph(383f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 76.1~80.2%"))
        stream.paragraph(452f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 0.3~0.4%"))
        stream.paragraph(519f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        stream.paragraph(88f,  y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "식도암"))
        stream.paragraph(205f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "식도-위내시경 검사"))
        stream.paragraph(316f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 79.0%"))
        stream.paragraph(383f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 62.0%"))
        stream.paragraph(452f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 0.2%"))
        stream.paragraph(519f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        stream.paragraph(88f,  y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      "난소암"))
        stream.paragraph(205f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "CA125 암표지자 검사"))
        stream.paragraph(316f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 95.0%"))
        stream.paragraph(383f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 43.3%"))
        stream.paragraph(452f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "약 0.6%"))
        stream.paragraph(519f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">98%"))

        y -= CONTENT_TITLE_RATE +10
        img = template.resource().imgContentTitle()
        width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(297f, y+10, 200f, AlignHorizontal.CENTER, TextBlock(style, "참 고 문 헌"))

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
                "6.\t대한소화기내시경학회지, 2007;35(2): 68-73."))

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
        stream.paragraph(56f, y, 500f, AlignHorizontal.LEFT, TextBlock(styleBold.clone().color(Color(67,72,142)).fontSize(8f), "검사의 임상적 의미"))
        stream.paragraph(56f, y-13f, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular.fontSize(7.5f), "본 검사는 검사 결과가 갖는 임상적 의미가 확립되지 않았으며, 이에 따르는 건강에 관련된 행위가 유용하다는 객관적 타당성이 아직 부족합니다.\n" +
                "- 간암, 폐암, 대장암, 췌장암, 식도암, 난소암에 대한 순환 종양 DNA 검사"))

        stream.paragraph(297f, y-60, 600f, AlignHorizontal.CENTER,
            TextBlock(template.resource().styleContentRegualar().clone().color(Color(151,151,151)).fontSize(7.5f), "※ 이 검사는 "),
            TextBlock(template.resource().styleContentRegualar().clone().color(Color(81,81,81)).fontSize(7.5f), "GC지놈에서 자체 개발한 검사(Laboratory-developed Test, LDT)로 적절한 평가를 통해 성능을 확인"),
            TextBlock(template.resource().styleContentRegualar().clone().color(Color(151,151,151)).fontSize(7.5f), "하였습니다.")
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
