package com.idrsys.ailis.cancerch.domain.report

import java.util.UUID

interface ReportRepository {
    suspend fun save(report: Report): Report
    suspend fun findById(id: Long): Report?
    suspend fun findByUuid(uuid: UUID): Report?
    suspend fun findBySampleIdAndServiceCode(sampleId: String, serviceCode: String): List<Report>
    suspend fun findAll(): List<Report>
    suspend fun findAllPaged(page: Int, size: Int, status: ReportStatus?): PagedReports
    suspend fun update(report: Report): Report
    suspend fun deleteById(id: Long)
}

data class PagedReports(
    val reports: List<Report>,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int,
    val pageSize: Int
)
