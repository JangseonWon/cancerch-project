package com.greencross.lims.report.cancerch.kokr

import com.greencross.lims.report.avoid.AvoidResource
import com.greencross.lims.report.cancerch.CancerchResourceN203
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import java.io.File

class CancerchResourceN203KoKr(doc: PDDocument) : CancerchResourceKoKr(doc), CancerchResourceN203 {
    val fontDefault: PDFont

    init {
        fontDefault = font(File(AvoidResource.resource, "font/NanumBarunGothic.ttf"))
    }

    override fun fontDefault(): PDFont {
        return fontDefault
    }
}