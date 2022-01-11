package com.greencross.lims.report.avoid

import java.awt.Color

interface AvoidResourceN201: AvoidResource {
    override fun colorPrimary(): Color { return Color.decode("0xF1C6D9") }
    override fun colorPrimaryStroke(): Color { return Color.decode("0xA42762") }
}