package com.greencross.lims.report.ON206.enus.resource

import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.resource.DNACTResourceON206
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.io.File

class DNACTResourceON206EnUs(doc: PDDocument): DNACTResourceEnUs(doc), DNACTResourceON206 {
    val fontDefault: PDFont

    init {
        fontDefault = font(File(DNACTResource.resource, "font/NanumBarunGothic.ttf"))
    }

    override fun fontDefault(): PDFont {
        return fontDefault
    }

    override fun imgTitle(): PDImageXObject = img(File(DNACTResource.resource, "img/avoid/SectionTitle/title_CT.png"))
}
