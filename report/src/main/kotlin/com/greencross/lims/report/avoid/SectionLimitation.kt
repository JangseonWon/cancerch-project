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
        stream.paragraph(305f, y+7, 200f, AlignHorizontal.CENTER, TextBlock(style, template.lblLimitationHeader()))

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
        img = template.resource().imgLimitationTable1()
        width = img.width * CONTENT_TABLE / img.height
        styleRegular = styleRegular.clone().color(Color(255,255,255)).fontSize(8f)
        styleBold = styleBold.clone().color(Color(255,255,255)).fontSize(9f)
        var styleRegularInner = styleRegular.clone().color(Color(81,81,81)).fontSize(8f)
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_TABLE)
        stream.paragraph(100f, y+115, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, template.lblLimitationTableHeader(0)))
        stream.paragraph(198f, y+115, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTableHeader(1)))
        stream.paragraph(300f, y+115, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTableHeader(2)))
        stream.paragraph(402f, y+115, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTableHeader(3)))
        stream.paragraph(502f, y+115, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTableHeader(4)))

        stream.paragraph(100f, y+100, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTableTotalCancer(0)))
        stream.paragraph(198f, y+100, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableTotalCancer(1)))
        stream.paragraph(300f, y+100, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableTotalCancer(2)))
        stream.paragraph(402f, y+100, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableTotalCancer(3)))
        stream.paragraph(502f, y+100, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableTotalCancer(4)))

        stream.paragraph(100f, y+84, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTableLiverCancer(0)))
        stream.paragraph(198f, y+84, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLiverCancer(1)))
        stream.paragraph(300f, y+84, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLiverCancer(2)))
        stream.paragraph(402f, y+84, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLiverCancer(3)))
        stream.paragraph(502f, y+84, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLiverCancer(4)))

        stream.paragraph(100f, y+68, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTableLungCancer(0)))
        stream.paragraph(198f, y+68, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLungCancer(1)))
        stream.paragraph(300f, y+68, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLungCancer(2)))
        stream.paragraph(402f, y+68, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLungCancer(3)))
        stream.paragraph(502f, y+68, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLungCancer(4)))

        stream.paragraph(100f, y+53, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTableColorCancer(0)))
        stream.paragraph(198f, y+53, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableColorCancer(1)))
        stream.paragraph(300f, y+53, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableColorCancer(2)))
        stream.paragraph(402f, y+53, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableColorCancer(3)))
        stream.paragraph(502f, y+53, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableColorCancer(4)))

        stream.paragraph(100f, y+37, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTablePanCancer(0)))
        stream.paragraph(198f, y+37, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTablePanCancer(1)))
        stream.paragraph(300f, y+37, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTablePanCancer(2)))
        stream.paragraph(402f, y+37, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTablePanCancer(3)))
        stream.paragraph(502f, y+37, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTablePanCancer(4)))

        stream.paragraph(100f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTableEsopCancer(0)))
        stream.paragraph(198f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableEsopCancer(1)))
        stream.paragraph(300f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableEsopCancer(2)))
        stream.paragraph(402f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableEsopCancer(3)))
        stream.paragraph(502f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableEsopCancer(4)))

        stream.paragraph(100f, y+7, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTableOverCancer(0)))
        stream.paragraph(198f, y+7, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableOverCancer(1)))
        stream.paragraph(300f, y+7, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableOverCancer(2)))
        stream.paragraph(402f, y+7, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableOverCancer(3)))
        stream.paragraph(502f, y+7, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableOverCancer(4)))

        y -= CONTENT_TABLE2 + 10
        img = template.resource().imgLimitationTable2()
        width = img.width * CONTENT_TABLE2 / img.height
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_TABLE2)
        stream.paragraph(100f, y+68, 100f, AlignHorizontal.CENTER, TextBlock(styleBold, template.lblLimitationTable2Header(0)))
        stream.paragraph(248f, y+68, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Header(1)))
        stream.paragraph(452f, y+68, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Header(2)))
        stream.paragraph(100f, y+53, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Row1(0)))
        stream.paragraph(248f, y+53, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row1(1)))
        stream.paragraph(452f, y+53, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row1(2)))
        stream.paragraph(100f, y+37, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Row2(0)))
        stream.paragraph(248f, y+37, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row2(1)))
        stream.paragraph(452f, y+37, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row2(2)))
        stream.paragraph(100f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Row3(0)))
        stream.paragraph(248f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row3(1)))
        stream.paragraph(452f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row3(2)))
        stream.paragraph(100f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Row4(0)))
        stream.paragraph(248f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row4(1)))
        stream.paragraph(452f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row4(2)))

        styleRegular = styleRegular.clone().color(Color(121,121,121)).fontSize(5.5f)
        y -= 4
        for(i in 0..3){
            y -= 8
            stream.paragraph(57f, y, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblLimitationDescription(i)))
        }

        y -= CONTENT_TITLE_RATE+20
        img = template.resource().imgContentTitle()
        width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(305f, y+7, 200f, AlignHorizontal.CENTER, TextBlock(style, template.lblReferenceTitle()))

        y -= CONTENT_REFERTABLE+10
        img = template.resource().imgReferenceTable()
        width = img.width * CONTENT_REFERTABLE / img.height
        styleRegular = styleRegular.clone().color(Color(81,81,81)).fontSize(6.5f)
        stream.drawImage(img, 305f-width/2, y, width, CONTENT_REFERTABLE)
        stream.paragraph(77f, y+30, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblReferenceLeft()))
        stream.paragraph(322f, y+30, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblReferenceRight()))

        y -= 35
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
        private const val CONTENT_TABLE = 125f
        private const val CONTENT_TABLE2 = 78f
    }
}