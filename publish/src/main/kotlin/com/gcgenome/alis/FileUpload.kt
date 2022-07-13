package com.gcgenome.alis

import java.io.Serializable
import java.time.LocalDate

data class FileUpload(
    val user: String,
    val request: Request,
    val data: ByteArray,
    val title: String,
    val fileType: String,
    val create: LocalDate,
    val desc: String
) : Serializable
