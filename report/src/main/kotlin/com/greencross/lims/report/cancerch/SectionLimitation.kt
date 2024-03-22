package com.greencross.lims.report.cancerch

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.AlignHorizontal
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionLimitation(private var y: Float = 755f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
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
        template.lblLimitationHeader(stream, y)

        y -= 10
        for(i in 0..6){
            y -= CONTENT_MINI_SQUARE+10
            img = template.resource().imgMiniSquare()
            width = img.width * CONTENT_MINI_SQUARE / img.height
            stream.drawImage(img, 50f-width/2, y, width, CONTENT_MINI_SQUARE)
            template.lblLimitation(stream, y, i)
        }

        y -= CONTENT_TABLE_TITLE+25
        img = template.resource().imgLimitationTableTitle()
        width = img.width * CONTENT_TABLE_TITLE / img.height
        stream.drawImage(img, 123f-width/2, y, width, CONTENT_TABLE_TITLE)
        stream.paragraph(123f, y+6, 300f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(Color(67,72,142)).fontSize(9f), template.lblPerformance()))

        y -= CONTENT_TABLE+10
        img = template.resource().imgLimitationTable1()
        width = img.width * CONTENT_TABLE / img.height
        styleRegular = styleRegular.clone().color(Color(255,255,255)).fontSize(8f)
        styleBold = styleBold.clone().color(Color(255,255,255)).fontSize(9f)
        var styleRegularInner = styleRegular.clone().color(Color(81,81,81)).fontSize(8f)
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TABLE)
        template.lblLimitationTableHeader(stream, y,0)
        template.lblLimitationTableHeader(stream, y,1)
        template.lblLimitationTableHeader(stream, y,2)
        template.lblLimitationTableHeader(stream, y,3)
        template.lblLimitationTableHeader(stream, y,4)

        stream.paragraph(88f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTableTotalCancer(dto!!.result, 0)))
        stream.paragraph(188f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableTotalCancer(dto.result, 1)))
        stream.paragraph(293f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableTotalCancer(dto.result, 2)))
        stream.paragraph(392f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableTotalCancer(dto.result, 3)))
        stream.paragraph(498f, y+102, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableTotalCancer(dto.result, 4)))

        stream.paragraph(88f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTableLungCancer(dto.result,0)))
        stream.paragraph(188f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLungCancer(dto.result,1)))
        stream.paragraph(293f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLungCancer(dto.result,2)))
        stream.paragraph(392f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLungCancer(dto.result,3)))
        stream.paragraph(498f, y+86, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLungCancer(dto.result,4)))

        stream.paragraph(88f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTableColorCancer(dto.result,0)))
        stream.paragraph(188f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableColorCancer(dto.result,1)))
        stream.paragraph(293f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableColorCancer(dto.result,2)))
        stream.paragraph(392f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableColorCancer(dto.result,3)))
        stream.paragraph(498f, y+70, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableColorCancer(dto.result,4)))

        stream.paragraph(88f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTableLiverCancer(dto.result,0)))
        stream.paragraph(188f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLiverCancer(dto.result,1)))
        stream.paragraph(293f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLiverCancer(dto.result,2)))
        stream.paragraph(392f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLiverCancer(dto.result,3)))
        stream.paragraph(498f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableLiverCancer(dto.result,4)))

        stream.paragraph(88f,  y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTablePanCancer(dto.result,0)))
        stream.paragraph(188f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTablePanCancer(dto.result,1)))
        stream.paragraph(293f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTablePanCancer(dto.result,2)))
        stream.paragraph(392f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTablePanCancer(dto.result,3)))
        stream.paragraph(498f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTablePanCancer(dto.result,4)))

        stream.paragraph(88f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTableEsopCancer(dto.result,0)))
        stream.paragraph(188f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableEsopCancer(dto.result,1)))
        stream.paragraph(293f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableEsopCancer(dto.result,2)))
        stream.paragraph(392f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableEsopCancer(dto.result,3)))
        stream.paragraph(498f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableEsopCancer(dto.result,4)))

        stream.paragraph(88f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular,      template.lblLimitationTableOverCancer(dto.result,0)))
        stream.paragraph(188f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableOverCancer(dto.result,1)))
        stream.paragraph(293f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableOverCancer(dto.result,2)))
        stream.paragraph(392f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableOverCancer(dto.result,3)))
        stream.paragraph(498f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTableOverCancer(dto.result,4)))

        y -= 4
        for(i in 0..3){
            y -= 8
            template.lblLimitationDescription(stream, y, i)
        }

        y -= CONTENT_TABLE_TITLE+20
        img = template.resource().imgLimitationTableTitle()
        styleRegular = styleRegular.clone().color(Color(255,255,255)).fontSize(8f)
        width = img.width * CONTENT_TABLE_TITLE / img.height
        stream.drawImage(img, 123f-width/2, y, width, CONTENT_TABLE_TITLE)
        stream.paragraph(123f, y+6, 300f, AlignHorizontal.CENTER, TextBlock(styleBold.clone().color(Color(67,72,142)).fontSize(9f), template.lblPerformanceBasic()))

        y -= CONTENT_TABLE2 + 7
        img = template.resource().imgLimitationTable2()
        width = img.width * CONTENT_TABLE2 / img.height
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TABLE2)
        stream.paragraph(88f,  y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Header(0)))
        stream.paragraph(205f, y+118, 150f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Header(1)))
        stream.paragraph(316f, y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Header(2)))
        stream.paragraph(383f, y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Header(3)))
        stream.paragraph(452f, y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Header(4)))
        stream.paragraph(519f, y+118, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Header(5)))

        stream.paragraph(88f,  y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Row1(0)))
        stream.paragraph(205f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row1(1)))
        stream.paragraph(316f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row1(2)))
        stream.paragraph(383f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row1(3)))
        stream.paragraph(452f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row1(4)))
        stream.paragraph(519f, y+101, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row1(5)))

        stream.paragraph(88f,  y+77, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Row2(0)))
        stream.paragraph(205f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row2(1)))
        stream.paragraph(316f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row2(2)))
        stream.paragraph(383f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row2(3)))
        stream.paragraph(452f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row2(4)))
        stream.paragraph(519f, y+85, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row2(5)))

        stream.paragraph(205f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row3(0)))
        stream.paragraph(316f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row3(1)))
        stream.paragraph(383f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row3(2)))
        stream.paragraph(452f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row3(3)))
        stream.paragraph(519f, y+69, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row3(4)))

        stream.paragraph(88f,  y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Row4(0)))
        stream.paragraph(205f, y+54, 150f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row4(1)))
        stream.paragraph(316f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row4(2)))
        stream.paragraph(383f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row4(3)))
        stream.paragraph(452f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row4(4)))
        stream.paragraph(519f, y+54, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row4(5)))

        stream.paragraph(88f,  y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Row5(0)))
        stream.paragraph(205f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row5(1)))
        stream.paragraph(316f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row5(2)))
        stream.paragraph(383f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row5(3)))
        stream.paragraph(452f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row5(4)))
        stream.paragraph(519f, y+38, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row5(5)))

        stream.paragraph(88f,  y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Row6(0)))
        stream.paragraph(205f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row6(1)))
        stream.paragraph(316f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row6(2)))
        stream.paragraph(383f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row6(3)))
        stream.paragraph(452f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row6(4)))
        stream.paragraph(519f, y+22, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row6(5)))

        stream.paragraph(88f,  y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegular, template.lblLimitationTable2Row7(0)))
        stream.paragraph(205f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row7(1)))
        stream.paragraph(316f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row7(2)))
        stream.paragraph(383f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row7(3)))
        stream.paragraph(452f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row7(4)))
        stream.paragraph(519f, y+6, 100f, AlignHorizontal.CENTER, TextBlock(styleRegularInner, template.lblLimitationTable2Row7(5)))

        y -= CONTENT_TITLE_RATE+20
        img = template.resource().imgContentTitle()
        width = img.width * CONTENT_TITLE_RATE / img.height
        stream.drawImage(img, 297f-width/2, y, width, CONTENT_TITLE_RATE)
        stream.paragraph(297f, y+10, 200f, AlignHorizontal.CENTER, TextBlock(style, template.lblReferenceTitle()))

        y -= 60
        stream.line(33f, y+42, 560f, y+42).setLineWidth(0.3f).setStrokingColor(Color(67,72,142)).stroke()
        stream.line(33f, y-36, 560f, y-36).setLineWidth(0.3f).setStrokingColor(Color(67,72,142)).stroke()
        stream.line(293f, y+32, 293f, y-26).setLineWidth(0.3f).setLineDashPattern(floatArrayOf(1f, 1.5f), 1f).setStrokingColor(Color.GRAY).stroke()
        styleRegular = styleRegular.clone().color(Color(81,81,81)).fontSize(6.5f)
        stream.paragraph(57f, y+22, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblReferenceLeft()))
        stream.paragraph(312f, y+22, 500f, AlignHorizontal.LEFT, TextBlock(styleRegular, template.lblReferenceRight()))

        y -= 50

        template.lblReferenceDescription(stream, y)
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
