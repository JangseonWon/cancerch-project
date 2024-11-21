package com.greencross.lims.report.kokr

import com.greencross.lims.report.HasSign
import com.gcgenome.lims.report.Template
import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.TextStyle
import com.greencross.lims.report.builder.AbstractReportDto
import com.greencross.lims.report.builder.Util
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

class SectionFooterGenomeLabs<T: Template<out HasSign>, D: AbstractReportDto> : Painter<T, D> {
    var img: PDImageXObject? = null
    val resource = File("/data/lims/resources")
    val color1 = Color(0, 54, 105)
    val color2 = Color(230, 0, 33)
    val color3 = Color(146,196,29)
    val color4 = Color(0,143,73)
    val address1 = "www.gcgenome.com 경기도 용인시 기흥구 이현로 30번길 107 대표전화 031-280-9900 상담톡 https://gccs.channel.io/"

    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: T,
        dto: D
    ): PDPageContentStreamPageAccessible {
        if(img == null){
            val src: BufferedImage = ImageIO.read(File(resource, "/img/footerLabsGenome.png"))
            val dest: BufferedImage = src.getSubimage(0, 0, src.width, src.height-80)
            val baos = ByteArrayOutputStream()
            ImageIO.write(dest, "png", baos)
            img = PDImageXObject.createFromByteArray(template.resource().doc(), baos.toByteArray(), "footer.png")
        }

        stream!!.saveGraphicsState();
        Util.icon(stream, img!!, 0f, 64f, 595f, 100f)
        stream.setLineWidth(0.01f)
            .setNonStrokingColor(color4).setStrokingColor(color4).moveTo(0f, 0f).lineTo(595f, 0f).lineTo(595f, 21.5f).lineTo(0f, 21.5f).fill()
            .setNonStrokingColor(color3).setStrokingColor(color3).moveTo(0f, 0f).lineTo(481f, 0f).lineTo(496f, 21.5f).lineTo(0f, 21.5f).fill()
            .setNonStrokingColor(color2).setStrokingColor(color2).moveTo(0f, 0f).lineTo(465f, 0f).lineTo(480f, 21.5f).lineTo(0f, 21.5f).fill()
            .setNonStrokingColor(color1).setStrokingColor(color1).moveTo(0f, 0f).lineTo(449f, 0f).lineTo(464f, 21.5f).lineTo(0f, 21.5f).fill()
        val font = TextStyle().color(Color.WHITE).fontSize(8f).paragraph(false)
        stream.paragraph(30f, 12f, 500f, TextBlock(font, address1))
        stream.restoreGraphicsState()
        return stream
    }

}
