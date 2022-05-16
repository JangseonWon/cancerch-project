package com.greencross.lims.report.avoid.kokr

import com.greencross.lims.report.TextStyle
import com.greencross.lims.report.avoid.AvoidResource
import com.greencross.lims.report.avoid.AvoidResourceN201
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.io.File

class AvoidResourceN201KoKr(doc: PDDocument) : AvoidResourceKoKr(doc), AvoidResourceN201 {
    val fontDefault: PDFont

    init {
        fontDefault = font(File(AvoidResource.resource, "font/NanumBarunGothic.ttf"))
    }
    override fun fontDefault(): PDFont {
        return fontDefault
    }
}