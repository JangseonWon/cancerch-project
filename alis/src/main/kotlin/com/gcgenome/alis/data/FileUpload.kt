package com.gcgenome.alis.data

import com.gcgenome.alis.service.Procedure
import java.io.Serializable
import java.time.LocalDate

data class FileUpload(
    val user: String,
    val request: Request,
    val data: ByteArray,
    val title: String,
    val fileType: Procedure.FileType,
    val create: LocalDate,
    val desc: String
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileUpload

        if (user != other.user) return false
        if (request != other.request) return false
        if (!data.contentEquals(other.data)) return false
        if (title != other.title) return false
        if (fileType != other.fileType) return false
        if (create != other.create) return false
        if (desc != other.desc) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + request.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + fileType.hashCode()
        result = 31 * result + create.hashCode()
        result = 31 * result + desc.hashCode()
        return result
    }
}
