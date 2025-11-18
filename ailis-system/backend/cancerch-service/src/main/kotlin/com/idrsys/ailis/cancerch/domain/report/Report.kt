package com.idrsys.ailis.cancerch.domain.report

import java.time.LocalDateTime
import java.util.UUID

data class Report(
    val id: Long? = null,
    val uuid: UUID = UUID.randomUUID(),
    val sampleId: String,
    val serviceCode: String,
    val batch: String,
    val rowNumber: Int,
    val reportName: String,
    val filePath: String,
    val fileSize: Long,
    val language: String = "ko",
    val status: ReportStatus = ReportStatus.GENERATED,
    val isPrinted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val publishedAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun publish(): Report {
        return copy(
            status = ReportStatus.PUBLISHED,
            publishedAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    fun markAsPrinted(): Report {
        return copy(
            isPrinted = true,
            updatedAt = LocalDateTime.now()
        )
    }
}

enum class ReportStatus {
    GENERATING,
    GENERATED,
    PUBLISHED,
    FAILED
}
