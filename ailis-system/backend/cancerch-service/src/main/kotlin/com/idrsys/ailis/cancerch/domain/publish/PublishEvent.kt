package com.idrsys.ailis.cancerch.domain.publish

import java.time.LocalDateTime
import java.util.UUID

data class PublishEvent(
    val eventId: UUID = UUID.randomUUID(),
    val reportId: Long,
    val sampleId: String,
    val serviceCode: String,
    val filePath: String,
    val eventType: PublishEventType = PublishEventType.REPORT_PUBLISHED,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val metadata: Map<String, Any> = emptyMap()
)

enum class PublishEventType {
    REPORT_PUBLISHED,
    REPORT_UPDATED,
    REPORT_DELETED
}
