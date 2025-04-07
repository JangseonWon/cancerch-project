package com.greencross.lims.report.enus

import com.greencross.lims.report.HasSign
import com.gcgenome.lims.report.Template
import com.greencross.lims.report.builder.AbstractReportDto
import com.greencross.lims.report.builder.Util
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

class SectionFooterEngGenomeNotColorBar<T: Template<out HasSign>, D: AbstractReportDto> : Painter<T, D> {
    var img: PDImageXObject? = null
    val resource = File("/data/lims/resources")

    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: T,
        dto: D
    ): PDPageContentStreamPageAccessible {
        if (img == null) {
            val src: BufferedImage = ImageIO.read(File(resource, "/img/footerEnUs.png"))
            val dest: BufferedImage = src.getSubimage(0, 0, src.width, src.height - 80)
            val baos = ByteArrayOutputStream()
            ImageIO.write(dest, "png", baos)
            img = PDImageXObject.createFromByteArray(template.resource().doc(), baos.toByteArray(), "footerEnUs.png")
        }

        stream!!.saveGraphicsState();
        Util.icon(stream, img!!, 0f, 64f, 595f, 95f)
        stream.restoreGraphicsState()
        return stream
    }
}
