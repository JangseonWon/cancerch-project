package com.idrsys.ailis.cancerch.application.publish.usecase

import com.idrsys.ailis.cancerch.application.publish.command.PublishReportCommand
import com.idrsys.ailis.cancerch.domain.publish.PublishEvent
import com.idrsys.ailis.cancerch.domain.publish.PublishEventType
import com.idrsys.ailis.cancerch.domain.publish.PublishRepository
import com.idrsys.ailis.cancerch.domain.report.Report
import com.idrsys.ailis.cancerch.domain.report.ReportRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PublishReportUseCase(
    private val reportRepository: ReportRepository,
    private val publishRepository: PublishRepository
) {
    @Transactional
    suspend fun execute(command: PublishReportCommand): Report {
        // Get report
        val report = reportRepository.findById(command.reportId)
            ?: throw IllegalArgumentException("Report not found: ${command.reportId}")

        // Publish report (update status)
        val publishedReport = report.publish()
        reportRepository.update(publishedReport)

        // Create and publish event
        val publishEvent = PublishEvent(
            reportId = publishedReport.id!!,
            sampleId = publishedReport.sampleId,
            serviceCode = publishedReport.serviceCode,
            filePath = publishedReport.filePath,
            eventType = PublishEventType.REPORT_PUBLISHED,
            metadata = command.metadata
        )

        publishRepository.publish(publishEvent)

        return publishedReport
    }
}
