package com.greencross.lims.report.ON204.resource

import java.awt.Color

interface DNACXResourceON204: DNACXResource {
    override fun colorPrimary(): Color { return Color.decode("0xF1C6D9") }
    override fun colorPrimaryStroke(): Color { return Color.decode("0xA42762") }
}
