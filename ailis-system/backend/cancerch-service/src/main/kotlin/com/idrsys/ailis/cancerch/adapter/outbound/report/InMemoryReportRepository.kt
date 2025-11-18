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

    override fun save(report: Report): Report {
        val id = idGenerator.getAndIncrement()
        val savedReport = report.copy(id = id)
        storage[id] = savedReport
        return savedReport
    }

    override fun findById(id: Long): Report? {
        return storage[id]
    }

    override fun findByUuid(uuid: UUID): Report? {
        return storage.values.find { it.uuid == uuid }
    }

    override fun findBySampleIdAndServiceCode(sampleId: String, serviceCode: String): List<Report> {
        return storage.values
            .filter { it.sampleId == sampleId && it.serviceCode == serviceCode }
            .sortedByDescending { it.createdAt }
    }

    override fun findAll(): List<Report> {
        return storage.values.sortedByDescending { it.createdAt }
    }

    override fun update(report: Report): Report {
        val id = report.id ?: throw IllegalArgumentException("Report ID cannot be null for update")
        if (!storage.containsKey(id)) {
            throw IllegalArgumentException("Report not found: $id")
        }
        storage[id] = report
        return report
    }

    override fun deleteById(id: Long) {
        storage.remove(id)
    }
}
