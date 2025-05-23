package com.gcgenome.lims.data

data class Worklist (
    val id: String = ""
) {
    var title:          String  = ""
    var createAt:       String  = ""
    var createBy:       String  = ""
    var status:         String  = ""
    var remark:         String  = ""
    var domain:         String  = ""
    var serial:         String  = ""
    var prefix:         String  = ""
    var idx:            Int     = -999999
    var lastModifyAt:   String  = ""
    var serializeBy:    String  = ""
}
