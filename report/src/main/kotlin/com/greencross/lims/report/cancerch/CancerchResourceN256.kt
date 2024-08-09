package com.greencross.lims.report.cancerch

import java.awt.Color

interface CancerchResourceN256: CancerchResource {
    override fun colorPrimary(): Color { return Color.decode("0xF1C6D9") }
    override fun colorPrimaryStroke(): Color { return Color.decode("0xA42762") }
}
