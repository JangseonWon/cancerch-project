package com.idrsys.ailis.cancerch.adapter.outbound.report

import com.idrsys.ailis.cancerch.domain.report.Report
import com.idrsys.ailis.cancerch.domain.report.ReportRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class InMemoryReportRepository : ReportRepository {

    private val storage = ConcurrentHashMap<Long, Report>()
    private val idGenerator = AtomicLong(1)

    override suspend fun save(report: Report): Report {
        val id = idGenerator.getAndIncrement()
        val savedReport = report.copy(id = id)
        storage[id] = savedReport
        return savedReport
    }

    override suspend fun findById(id: Long): Report? {
        return storage[id]
    }

    override suspend fun findByUuid(uuid: UUID): Report? {
        return storage.values.find { it.uuid == uuid }
    }

    override suspend fun findBySampleIdAndServiceCode(sampleId: String, serviceCode: String): List<Report> {
        return storage.values
            .filter { it.sampleId == sampleId && it.serviceCode == serviceCode }
            .sortedByDescending { it.createdAt }
    }

    override suspend fun findAll(): List<Report> {
        return storage.values.sortedByDescending { it.createdAt }
    }

    override suspend fun findAllPaged(page: Int, size: Int, status: com.idrsys.ailis.cancerch.domain.report.ReportStatus?): com.idrsys.ailis.cancerch.domain.report.PagedReports {
        // Filter by status if provided
        val filtered = if (status != null) {
            storage.values.filter { it.status == status }
        } else {
            storage.values.toList()
        }

        // Sort by createdAt descending
        val sorted = filtered.sortedByDescending { it.createdAt }

        // Calculate pagination
        val totalElements = sorted.size.toLong()
        val totalPages = ((totalElements + size - 1) / size).toInt()
        val offset = page * size
        val paged = sorted.drop(offset).take(size)

        return com.idrsys.ailis.cancerch.domain.report.PagedReports(
            reports = paged,
            totalElements = totalElements,
            totalPages = totalPages,
            currentPage = page,
            pageSize = size
        )
    }

    override suspend fun update(report: Report): Report {
        val id = report.id ?: throw IllegalArgumentException("Report ID cannot be null for update")
        if (!storage.containsKey(id)) {
            throw IllegalArgumentException("Report not found: $id")
        }
        storage[id] = report
        return report
    }

    override suspend fun deleteById(id: Long) {
        storage.remove(id)
    }
}
