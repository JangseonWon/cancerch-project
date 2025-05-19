package com.greencross.lims.report.ON204

import com.gcgenome.lims.report.Template
import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.HasSign
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.template.DNACXTemplate
import com.greencross.lims.report.builder.AbstractReportDto
import com.greencross.lims.report.builder.Util
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

class SectionFooterWithLymphotec<T: Template<out HasSign>, D: AbstractReportDto> : Painter<T, D> {
    var img: PDImageXObject? = null
    val resource = File("/data/lims/resources")

    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: T,
        dto: D
    ): PDPageContentStreamPageAccessible {
        if (img == null) {
            val src: BufferedImage = ImageIO.read(File(resource, "/img/footerLymphotec.png"))
            val baos = ByteArrayOutputStream()
            ImageIO.write(src, "png", baos)
            img = PDImageXObject.createFromByteArray(template.resource().doc(), baos.toByteArray(), "footer.png")
        }

        stream!!.saveGraphicsState();
        Util.icon(stream, img!!, 0f, 64f, 595f, 100f)
        stream.restoreGraphicsState()
        return stream
    }
}
