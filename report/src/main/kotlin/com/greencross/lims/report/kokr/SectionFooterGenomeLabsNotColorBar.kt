package com.greencross.lims.report.kokr

import com.greencross.lims.report.HasSign
import com.gcgenome.lims.report.Template
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

class SectionFooterGenomeLabsNotColorBar<T: Template<out HasSign>, D: AbstractReportDto> : Painter<T, D> {
    var img: PDImageXObject? = null
    val resource = File("/data/lims/resources")

    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: T,
        dto: D
    ): PDPageContentStreamPageAccessible {
        if (img == null) {
            val src: BufferedImage = ImageIO.read(File(resource, "/img/footerLabsGenome.png"))
            val baos = ByteArrayOutputStream()
            ImageIO.write(src, "png", baos)
            img = PDImageXObject.createFromByteArray(template.resource().doc(), baos.toByteArray(), "footer.png")
        }

        stream!!.saveGraphicsState();
        Util.icon(stream, img!!, 0f, 64f, 595f, 100f)
        stream.setLineWidth(5f).setStrokingColor(Color(84, 151, 87)).setNonStrokingColor(Color(84, 151, 87)).moveTo(25f, 59f).lineTo(565f,59f).fill()
        stream.restoreGraphicsState()
        return stream
    }
}
