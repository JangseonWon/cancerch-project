package com.idrsys.ailis.cancerch.adapter.inbound.report

import com.idrsys.ailis.cancerch.application.report.command.GenerateReportCommand
import com.idrsys.ailis.cancerch.application.report.usecase.GenerateReportUseCase
import com.idrsys.ailis.cancerch.application.report.usecase.GetReportUseCase
import com.idrsys.ailis.cancerch.domain.report.Report
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reports")
class ReportController(
    private val generateReportUseCase: GenerateReportUseCase,
    private val getReportUseCase: GetReportUseCase
) {

    @GetMapping("/samples/{sampleId}/services/{serviceCode}")
    fun getReportsBySampleAndService(
        @PathVariable sampleId: String,
        @PathVariable serviceCode: String
    ): ResponseEntity<List<ReportResponse>> {
        val reports = getReportUseCase.getBySampleAndService(sampleId, serviceCode)
        return ResponseEntity.ok(reports.map { it.toResponse() })
    }

    @GetMapping("/{id}")
    fun getReportById(@PathVariable id: Long): ResponseEntity<ReportResponse> {
        return try {
            val report = getReportUseCase.getById(id)
            ResponseEntity.ok(report.toResponse())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAllReports(): ResponseEntity<List<ReportResponse>> {
        val reports = getReportUseCase.getAllReports()
        return ResponseEntity.ok(reports.map { it.toResponse() })
    }

    @PostMapping("/generate")
    fun generateReport(@RequestBody request: GenerateReportRequest): ResponseEntity<ReportResponse> {
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
