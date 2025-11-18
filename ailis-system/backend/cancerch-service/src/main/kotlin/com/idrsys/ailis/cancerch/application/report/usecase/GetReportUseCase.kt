package com.idrsys.ailis.cancerch.application.report.usecase

import com.idrsys.ailis.cancerch.domain.report.PagedReports
import com.idrsys.ailis.cancerch.domain.report.Report
import com.idrsys.ailis.cancerch.domain.report.ReportRepository
import com.idrsys.ailis.cancerch.domain.report.ReportStatus
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetReportUseCase(
    private val reportRepository: ReportRepository
) {
    suspend fun getById(id: Long): Report {
        return reportRepository.findById(id)
            ?: throw IllegalArgumentException("Report not found: $id")
    }

    suspend fun getByUuid(uuid: UUID): Report {
        return reportRepository.findByUuid(uuid)
            ?: throw IllegalArgumentException("Report not found: $uuid")
    }

    suspend fun getBySampleAndService(sampleId: String, serviceCode: String): List<Report> {
        return reportRepository.findBySampleIdAndServiceCode(sampleId, serviceCode)
    }

    suspend fun getAllReports(): List<Report> {
        return reportRepository.findAll()
    }

    suspend fun getReportsPaged(page: Int, size: Int, status: ReportStatus?): PagedReports {
        return reportRepository.findAllPaged(page, size, status)
    }
}
