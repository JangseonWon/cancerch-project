package com.gcgenome.lims.data

import java.time.LocalDateTime

data class Report(
    var sample:             Long?,
    var service:            String?,
    var createAt:           String?,
) {
    var fileName:           String?         = null
    var fileSize:           Int?            = null
    var fileUrl:            String?         = null
    var createBy:           User?           = null
    var publishAt:          LocalDateTime?  = null
    var publishBy:          User?           = null
    var description:        String?         = null
    var reportResult:       String?         = null
    var reportResultType:   String?         = null
    var reportComment:      String?         = null
    var reportText:         String?         = null
}

