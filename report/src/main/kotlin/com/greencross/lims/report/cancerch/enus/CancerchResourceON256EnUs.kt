package com.greencross.lims.report.cancerch.enus

import com.greencross.lims.report.avoid.AvoidResource
import com.greencross.lims.report.cancerch.CancerchResource
import com.greencross.lims.report.cancerch.CancerchResourceON203
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.io.File

class CancerchResourceON256EnUs(doc: PDDocument) : CancerchResourceEnUs(doc), CancerchResourceON203 {
    val fontDefault: PDFont

    init {
        fontDefault = font(File(AvoidResource.resource, "font/NanumBarunGothic.ttf"))
    }

    override fun fontDefault(): PDFont {
        return fontDefault
    }
    override fun imgTitle()       : PDImageXObject = img(File(CancerchResource.resource, "img/avoid/SectionTitle/0_title_gangbuk_EnUs.png"))
    override fun imgGuideLineTable(risk: String) = when(risk){
        "RISK"   -> img(File(CancerchResource.resource, "img/avoid/SectionGuideLine/img_table_n203.png"))
        "OTHERS" -> img(File(CancerchResource.resource, "img/avoid/SectionGuideLine/img_table_n203.png"))
        else     -> img(File(CancerchResource.resource, "img/avoid/SectionGuideLine/img_table_n256.png"))
    }
    override fun imgGuideLineTotalCancer() : PDImageXObject { return img(File(CancerchResource.resource, "img/avoid/SectionGuideLine/img_table_on256_general.png")) }
}
