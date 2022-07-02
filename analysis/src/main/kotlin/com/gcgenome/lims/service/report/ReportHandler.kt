package com.gcgenome.lims.service.report

import com.gcgenome.lims.projection.Report
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux

@Service
class ReportHandler(
    private val dao: ReportDao,
) {
    @Transactional(readOnly = true)
    fun reports(sample:Long, service: String): Flux<Report> = dao.findBySampleAndService(sample, service)
}