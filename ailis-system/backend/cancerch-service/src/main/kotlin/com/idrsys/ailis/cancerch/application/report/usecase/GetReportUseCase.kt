package com.idrsys.ailis.cancerch.application.report.usecase

import com.idrsys.ailis.cancerch.domain.report.Report
import com.idrsys.ailis.cancerch.domain.report.ReportRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetReportUseCase(
    private val reportRepository: ReportRepository
) {
    fun getById(id: Long): Report {
        return reportRepository.findById(id)
            ?: throw IllegalArgumentException("Report not found: $id")
    }

    fun getByUuid(uuid: UUID): Report {
        return reportRepository.findByUuid(uuid)
            ?: throw IllegalArgumentException("Report not found: $uuid")
    }

    fun getBySampleAndService(sampleId: String, serviceCode: String): List<Report> {
        return reportRepository.findBySampleIdAndServiceCode(sampleId, serviceCode)
    }

    fun getAllReports(): List<Report> {
        return reportRepository.findAll()
    }
}
