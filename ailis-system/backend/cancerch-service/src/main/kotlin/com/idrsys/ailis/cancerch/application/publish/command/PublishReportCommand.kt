package com.idrsys.ailis.cancerch.application.publish.command

data class PublishReportCommand(
    val reportId: Long,
    val metadata: Map<String, Any> = emptyMap()
)
