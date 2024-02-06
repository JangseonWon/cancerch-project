package com.greencross.lims.report.enus

import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Color

interface HasHeaderEnUs {
    fun colorGray(): Color
    fun colorText(): Color

    fun fontHeader(): PDFont
    fun fontHeaderValue(): PDFont
    fun fontDefault(): PDFont
}