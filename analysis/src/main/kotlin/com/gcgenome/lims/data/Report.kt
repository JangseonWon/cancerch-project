package com.gcgenome.lims.data

import java.time.LocalDateTime

data class Report(
    var sample: Long? = 0,
    var service: String? = "",
    var createAt: String? = "",
    var fileName: String? = "",
    var fileSize: Int? = 0,
    var fileUrl: String? = null,
    var createBy: User? = null,
    var publishAt: LocalDateTime? = null,
    var publisher: User? = null,
    var description: String? = ""
) {
    var isPrinted: String = ""
}

