package com.idrsys.ailis.cancerch.adapter.inbound.report

import com.idrsys.ailis.cancerch.application.publish.command.PublishReportCommand
import com.idrsys.ailis.cancerch.application.publish.usecase.PublishReportUseCase
import com.idrsys.ailis.cancerch.application.report.command.GenerateReportCommand
import com.idrsys.ailis.cancerch.application.report.usecase.GenerateReportUseCase
import com.idrsys.ailis.cancerch.application.report.usecase.GetReportUseCase
import com.idrsys.ailis.cancerch.domain.report.Report
import com.idrsys.ailis.cancerch.domain.report.ReportStatus
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reports")
class ReportController(
    private val generateReportUseCase: GenerateReportUseCase,
    private val getReportUseCase: GetReportUseCase,
    private val publishReportUseCase: PublishReportUseCase
) {

    @GetMapping("/samples/{sampleId}/services/{serviceCode}")
    suspend fun getReportsBySampleAndService(
        @PathVariable sampleId: String,
        @PathVariable serviceCode: String
    ): ResponseEntity<List<ReportResponse>> {
        val reports = getReportUseCase.getBySampleAndService(sampleId, serviceCode)
        return ResponseEntity.ok(reports.map { it.toResponse() })
    }

    @GetMapping("/{id}")
    suspend fun getReportById(@PathVariable id: Long): ResponseEntity<ReportResponse> {
        return try {
            val report = getReportUseCase.getById(id)
            ResponseEntity.ok(report.toResponse())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    suspend fun getAllReports(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) status: String?
    ): ResponseEntity<PagedReportResponse> {
        val reportStatus = status?.let {
            try {
                ReportStatus.valueOf(it.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        val pagedReports = getReportUseCase.getReportsPaged(page, size, reportStatus)
        val response = PagedReportResponse(
            reports = pagedReports.reports.map { it.toResponse() },
            totalElements = pagedReports.totalElements,
            totalPages = pagedReports.totalPages,
            currentPage = pagedReports.currentPage,
            pageSize = pagedReports.pageSize
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping
    suspend fun generateReport(@RequestBody request: GenerateReportRequest): ResponseEntity<ReportResponse> {
        val command = GenerateReportCommand(
            sampleId = request.sampleId,
            serviceCode = request.serviceCode,
            batch = request.batch,
            rowNumber = request.rowNumber,
            language = request.language ?: "ko"
        )

        val report = generateReportUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(report.toResponse())
    }

    @PostMapping("/{id}/publish")
    suspend fun publishReport(
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

    private fun Report.toResponse() = ReportResponse(
        id = this.id!!,
        uuid = this.uuid.toString(),
        sampleId = this.sampleId,
        serviceCode = this.serviceCode,
        batch = this.batch,
        rowNumber = this.rowNumber,
        reportName = this.reportName,
        filePath = this.filePath,
        fileSize = this.fileSize,
        language = this.language,
        status = this.status.name,
        isPrinted = this.isPrinted,
        createdAt = this.createdAt.toString(),
        publishedAt = this.publishedAt?.toString()
    )
}

data class GenerateReportRequest(
    val sampleId: String,
    val serviceCode: String,
    val batch: String,
    val rowNumber: Int,
    val language: String? = "ko"
)

data class ReportResponse(
    val id: Long,
    val uuid: String,
    val sampleId: String,
    val serviceCode: String,
    val batch: String,
    val rowNumber: Int,
    val reportName: String,
    val filePath: String,
    val fileSize: Long,
    val language: String,
    val status: String,
    val isPrinted: Boolean,
    val createdAt: String,
    val publishedAt: String?
)

data class PagedReportResponse(
    val reports: List<ReportResponse>,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int,
    val pageSize: Int
)

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
