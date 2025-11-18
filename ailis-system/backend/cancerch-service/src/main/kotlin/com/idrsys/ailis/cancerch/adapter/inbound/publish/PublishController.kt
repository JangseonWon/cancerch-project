package com.idrsys.ailis.cancerch.adapter.inbound.publish

import com.idrsys.ailis.cancerch.application.publish.command.PublishReportCommand
import com.idrsys.ailis.cancerch.application.publish.usecase.PublishReportUseCase
import com.idrsys.ailis.cancerch.domain.report.Report
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/publish")
class PublishController(
    private val publishReportUseCase: PublishReportUseCase
) {

    @PostMapping("/reports/{id}")
    fun publishReport(
        @PathVariable id: Long,
        @RequestBody(required = false) request: PublishRequest?
    ): ResponseEntity<PublishResponse> {
        return try {
            val command = PublishReportCommand(
                reportId = id,
                metadata = request?.metadata ?: emptyMap()
            )

            val publishedReport = publishReportUseCase.execute(command)

            ResponseEntity.ok(
                PublishResponse(
                    success = true,
                    message = "Report published successfully",
                    reportId = publishedReport.id!!,
                    status = publishedReport.status.name,
                    publishedAt = publishedReport.publishedAt?.toString()
                )
            )
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(
                PublishResponse(
                    success = false,
                    message = e.message ?: "Failed to publish report",
                    reportId = id,
                    status = null,
                    publishedAt = null
                )
            )
        }
    }
}

data class PublishRequest(
    val metadata: Map<String, Any> = emptyMap()
)

data class PublishResponse(
    val success: Boolean,
    val message: String,
    val reportId: Long,
    val status: String?,
    val publishedAt: String?
)
