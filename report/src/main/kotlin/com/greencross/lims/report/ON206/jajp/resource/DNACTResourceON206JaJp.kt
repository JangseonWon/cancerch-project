package com.greencross.lims.report.ON206.jajp.resource

import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.resource.DNACTResourceON206
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.io.File

class DNACTResourceON206JaJp(doc: PDDocument): DNACTResourceJaJp(doc), DNACTResourceON206 {
    val fontDefault: PDFont

    init {
        fontDefault = font(File(DNACTResource.resource, "font/NotoSansJP-Regular.ttf"))
    }

    override fun fontDefault(): PDFont {
        return fontDefault
    }

    override fun imgTitle(): PDImageXObject = img(File(DNACTResource.resource, "img/avoid/SectionTitle/title_CT.png"))
}
