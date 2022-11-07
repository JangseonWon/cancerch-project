package com.greencross.lims.service.report

import com.gcgenome.querydsl.PersistQuerydslR2dbcRepo
import com.greencross.lims.entity.Report
import reactor.core.publisher.Flux

interface ReportRepository : PersistQuerydslR2dbcRepo<Report, Report.Companion.ReportPK> {
    fun findTop5ByIsPrinted(isPrinted: String): Flux<Report>
    fun findAllByIsPrinted(isPrinted: String): Flux<Report>
}