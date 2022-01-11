package com.greencross.lims.report.avoid.kokr

import com.greencross.lims.report.avoid.AvoidResource
import com.greencross.lims.report.avoid.AvoidResourceN201
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.io.File

class AvoidResourceN201KoKr(doc: PDDocument) : AvoidResourceKoKr(doc), AvoidResourceN201 {
    val fontDefault: PDFont
    val img: File = File(AvoidResource.resource, "img/Avoid/N201/KoKr")
    val imgTitle = img(File(img, "title.png"))
    val imgHeaderLogo = img(File(img, "header.png"))

    init {
        fontDefault = font(File(AvoidResource.resource, "font/NanumBarunGothic.ttf"))
    }
    override fun fontDefault(): PDFont {
        return fontDefault
    }
    override fun imgTitle(): PDImageXObject {
        return imgTitle
    }
    override fun imgHeaderLogo(): PDImageXObject {
        return imgHeaderLogo
    }

}