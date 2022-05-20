package com.greencross.lims.report.avoid

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionLimitation (private var y: Float = 685f)  : Painter<AvoidTemplate<AvoidResource>, AvoidDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: AvoidTemplate<AvoidResource>?,
        dto: AvoidDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        var img = template!!.resource().imgContentTitle()
        var width = img.width * CONTENT_TITLE_RATE / img.height
        var style = template.resource().styleContentSpecial().clone().color(Color(255, 255, 255)).fontSize(12f)
        var styleBold = template.resource().styleContentBold().clone().fontSize(10f)
        var styleRegular = template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(7f)
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(305f, y+7, 200f, AlignHorizontal.CENTER, TextBlock(style, "검 사 한 계"))

        y -= 10
        for(i in 0..5){
            y -= CONTENT_MINI_SQUARE+10
            img = template.resource().imgMiniSquare()
            width = img.width * CONTENT_MINI_SQUARE / img.height
            stream.drawImage(img, 75f-width/2, y, width, CONTENT_MINI_SQUARE)
            stream.paragraph(85f, y, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblLimitation(i)))
        }

        y -= CONTENT_TABLE_TITLE+30
        img = template.resource().imgLimitationTableTitle()
        width = img.width * CONTENT_TABLE_TITLE / img.height
        stream.drawImage(img, 137f-width/2, y, width, CONTENT_TABLE_TITLE)
        stream.paragraph(137f, y+7, 300f, AlignHorizontal.CENTER, TextBlock(styleRegular.clone().color(Color(67,72,142)).fontSize(8f), template.lblPerformance()))

        y -= CONTENT_TABLE+10
        img = template.resource().imgLimitationTable()
        width = img.width * CONTENT_TABLE / img.height
        styleRegular = styleRegular.clone().color(Color(255,255,255)).fontSize(8f)
        styleBold = styleBold.clone().color(Color(255,255,255)).fontSize(9f)
        var styleRegularInner = styleRegular.clone().color(Color(81,81,81)).fontSize(8f)
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_TABLE)
        stream.paragraph(100f, y+215, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "암종"))
        stream.paragraph(198f, y+215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "특이도¹⁾"))
        stream.paragraph(300f, y+215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "민감도²⁾"))
        stream.paragraph(402f, y+215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "양성예측도³⁾"))
        stream.paragraph(502f, y+215, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "음성예측도⁴⁾"))

        stream.paragraph(100f, y+200, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "전체"))
        stream.paragraph(198f, y+200, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "99%"))
        stream.paragraph(300f, y+200, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "84.60%"))
        stream.paragraph(402f, y+200, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "72.95%"))
        stream.paragraph(502f, y+200, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">99%"))

        stream.paragraph(100f, y+184, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "폐암"))
        stream.paragraph(198f, y+184, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "99%"))
        stream.paragraph(300f, y+184, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "73.10%"))
        stream.paragraph(402f, y+184, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "31.25%"))
        stream.paragraph(502f, y+184, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">99%"))

        stream.paragraph(100f, y+168, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "대장암"))
        stream.paragraph(350f, y+168, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "미정"))

        stream.paragraph(100f, y+153, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "간암"))
        stream.paragraph(198f, y+153, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "99%"))
        stream.paragraph(300f, y+153, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "97.80%"))
        stream.paragraph(402f, y+153, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "27.28%"))
        stream.paragraph(502f, y+153, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">99%"))

        stream.paragraph(100f, y+137, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "췌장암"))
        stream.paragraph(198f, y+137, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "99%"))
        stream.paragraph(300f, y+137, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "76.50%"))
        stream.paragraph(402f, y+137, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "5.65%"))
        stream.paragraph(502f, y+137, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">99%"))

        stream.paragraph(100f, y+122, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "식도암"))
        stream.paragraph(198f, y+122, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "99%"))
        stream.paragraph(300f, y+122, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "66.70%"))
        stream.paragraph(402f, y+122, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "4.78%"))
        stream.paragraph(502f, y+122, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">99%"))

        stream.paragraph(100f, y+106, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "유방암"))
        stream.paragraph(198f, y+106, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "99%"))
        stream.paragraph(300f, y+106, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "85.20%"))
        stream.paragraph(402f, y+106, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "41.98%"))
        stream.paragraph(502f, y+106, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">99%"))

        stream.paragraph(100f, y+91, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "난소암"))
        stream.paragraph(198f, y+91, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "99%"))
        stream.paragraph(300f, y+91, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "83.30%"))
        stream.paragraph(402f, y+91, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "6.04%"))
        stream.paragraph(502f, y+91, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, ">99%"))

        stream.paragraph(100f, y+68, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, "병기"))
        stream.paragraph(248f, y+68, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "특이도"))
        stream.paragraph(452f, y+68, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "민감도"))
        stream.paragraph(100f, y+52, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "1기"))
        stream.paragraph(248f, y+52, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "99%"))
        stream.paragraph(452f, y+52, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "63.60%"))
        stream.paragraph(100f, y+37, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "2기"))
        stream.paragraph(248f, y+37, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "99%"))
        stream.paragraph(452f, y+37, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "74.50%"))
        stream.paragraph(100f, y+21, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "3기"))
        stream.paragraph(248f, y+21, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "99%"))
        stream.paragraph(452f, y+21, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "91.80%"))
        stream.paragraph(100f, y+5, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, "4기"))
        stream.paragraph(248f, y+5, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "99%"))
        stream.paragraph(452f, y+5, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, "83.10%"))

        styleRegular = styleRegular.clone().color(Color(121,121,121)).fontSize(5.5f)
        y -= 4
        for(i in 0..3){
            y -= 8
            stream.paragraph(57f, y, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblLimitationDescription(i)))
        }

        y -= CONTENT_TITLE_RATE+30
        img = template.resource().imgContentTitle()
        width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(305f, y+7, 200f, AlignHorizontal.CENTER, TextBlock(style, "참 고 문 헌"))

        y -= CONTENT_REFERTABLE+20
        img = template.resource().imgReferenceTable()
        width = img.width * CONTENT_REFERTABLE / img.height
        styleRegular = styleRegular.clone().color(Color(81,81,81)).fontSize(6.5f)
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_REFERTABLE)
        stream.paragraph(77f, y+30, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblReferenceLeft()))
        stream.paragraph(322f, y+30, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblReferenceRight()))

        y -= 20
        styleRegular = styleRegular.clone().color(Color(151,151,151)).fontSize(6.5f)
        styleRegularInner = styleRegular.clone().color(Color(81,81,81)).fontSize(6.5f)
        stream.paragraph(305f, y, 600f, AlignHorizontal.CENTER,
            TextBlock(styleRegular, template.lblReferenceDescription(0)),
            TextBlock(styleRegularInner, template.lblReferenceDescriptionBold()),
            TextBlock(styleRegular, template.lblReferenceDescription(1))
            )
        return stream
    }
    companion object {
        private const val CONTENT_TITLE_RATE = 21f
        private const val CONTENT_REFERTABLE = 48f
        private const val CONTENT_TABLE_TITLE = 18f
        private const val CONTENT_MINI_SQUARE = 4f
        private const val CONTENT_TABLE = 225f
    }
}