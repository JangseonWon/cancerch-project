package com.greencross.lims.report.ON204.enus.resource

import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.ON204.resource.DNACXResourceON204
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.io.File

class DNACXResourceON204EnUs(doc: PDDocument): DNACXResourceEnUs(doc), DNACXResourceON204 {
    val fontDefault: PDFont

    init {
        fontDefault = font(File(DNACXResource.resource, "font/NanumBarunGothic.ttf"))
    }

    override fun fontDefault(): PDFont {
        return fontDefault
    }

    override fun imgTitle(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionTitle/0_title.png"))
}
