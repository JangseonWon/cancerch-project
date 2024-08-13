package com.greencross.lims.report.cancerch.kokr

import com.greencross.lims.report.avoid.AvoidResource
import com.greencross.lims.report.cancerch.CancerchResource
import com.greencross.lims.report.cancerch.CancerchResourceN203
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.io.File

class CancerchResourceN203KoKr(doc: PDDocument) : CancerchResourceKoKr(doc), CancerchResourceN203 {
    val fontDefault: PDFont

    init {
        fontDefault = font(File(AvoidResource.resource, "font/NanumBarunGothic.ttf"))
    }

    override fun fontDefault()    : PDFont = fontDefault
    override fun imgTitle()       : PDImageXObject = img(File(CancerchResource.resource, "img/avoid/SectionTitle/0_title.png"))
    override fun imgGuideLineTotalCancer() : PDImageXObject { return img(File(CancerchResource.resource, "img/avoid/SectionGuideLine/img_table_n203_general.png")) }
    override fun imgGuideLineTable(risk: String) = when(risk){
        "RISK"   -> img(File(CancerchResource.resource, "img/avoid/SectionGuideLine/img_table_n203.png"))
        "OTHERS" -> img(File(CancerchResource.resource, "img/avoid/SectionGuideLine/img_table_n203.png"))
        else     -> img(File(CancerchResource.resource, "img/avoid/SectionGuideLine/img_table_n203_middle.png"))
    }
}
