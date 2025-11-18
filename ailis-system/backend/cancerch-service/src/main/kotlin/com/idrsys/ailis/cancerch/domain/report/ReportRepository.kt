package com.idrsys.ailis.cancerch.domain.report

import java.util.UUID

interface ReportRepository {
    fun save(report: Report): Report
    fun findById(id: Long): Report?
    fun findByUuid(uuid: UUID): Report?
    fun findBySampleIdAndServiceCode(sampleId: String, serviceCode: String): List<Report>
    fun findAll(): List<Report>
    fun update(report: Report): Report
    fun deleteById(id: Long)
}
