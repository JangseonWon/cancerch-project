package com.gcgenome.lims.data

data class Sample(
    val id: Long
) {
    var patient: Patient?   = null
    var barcode: String?    = ""

}