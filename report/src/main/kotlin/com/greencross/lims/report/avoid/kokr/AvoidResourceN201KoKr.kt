package com.greencross.lims.report.avoid.kokr

import com.greencross.lims.report.avoid.AvoidResourceN201
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import java.io.File

class AvoidResourceN201KoKr(doc: PDDocument) : AvoidResourceKoKr(doc), AvoidResourceN201 {
    val fontDefault: PDFont

    val img: File = File(AvoidResourceN201, "img/Avoid/N201/KoKr")
    init {
        fontDefault = font(File(AvoidResourceN201))
    }

}