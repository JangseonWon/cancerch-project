package com.greencross.lims.report.kokr

import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Color

interface HasHeaderKoKr {
    fun colorGray(): Color
    fun colorText(): Color

    fun fontHeader(): PDFont
    fun fontHeaderValue(): PDFont
    fun fontDefault(): PDFont
}