package com.idrsys.ailis.cancerch.application.report.usecase

import com.idrsys.ailis.cancerch.application.report.command.GenerateReportCommand
import com.idrsys.ailis.cancerch.domain.report.Report
import com.idrsys.ailis.cancerch.domain.report.ReportRepository
import com.idrsys.ailis.cancerch.domain.report.ReportStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GenerateReportUseCase(
    private val reportRepository: ReportRepository
) {
    suspend fun execute(command: GenerateReportCommand): Report {
        // Generate report file path
        val fileName = "${command.sampleId}_${command.serviceCode}_${System.currentTimeMillis()}.pdf"
        val filePath = "/reports/${command.batch}/${fileName}"

        val report = Report(
            sampleId = command.sampleId,
            serviceCode = command.serviceCode,
            batch = command.batch,
            rowNumber = command.rowNumber,
            reportName = fileName,
            filePath = filePath,
            fileSize = 1024L, // Mock file size
            language = command.language,
            status = ReportStatus.GENERATED,
            createdAt = LocalDateTime.now()
        )

        return reportRepository.save(report)
    }
}
