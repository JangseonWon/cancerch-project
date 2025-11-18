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
    val generatedAt: LocalDateTime? = null,
    val publishedAt: LocalDateTime? = null,
    val publishedBy: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val version: Int = 1
) {
    fun publish(publishedBy: String? = null): Report {
        return copy(
            status = ReportStatus.PUBLISHED,
            publishedAt = LocalDateTime.now(),
            publishedBy = publishedBy,
            updatedAt = LocalDateTime.now(),
            version = version + 1
        )
    }

    fun markAsPrinted(): Report {
        return copy(
            isPrinted = true,
            updatedAt = LocalDateTime.now(),
            version = version + 1
        )
    }
}

enum class ReportStatus {
    GENERATING,
    GENERATED,
    PUBLISHED,
    FAILED
}
