package com.gcgenome.lims.data

data class Request(
    var sample: Sample
){
    var serial: String          = ""
    var service: Service?       = null
    var dateRequest: String     = ""
    var dateStart: String       = ""
    var dateSampling: String    = ""
    var dateDue: String         = ""
    var registered: String      = ""
    var canceled: String        = ""
    var deleted: String         = ""
}