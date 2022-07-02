package com.gcgenome.lims.service.report

import com.gcgenome.lims.entity.QReport.report
import com.gcgenome.lims.entity.QUser
import com.gcgenome.lims.projection.Report
import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQuery
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ReportDao(private val repo: ReportRepository) {
    private val createBy = QUser("Creator")
    private val modifyBy = QUser("Modifier")
    private val publishBy = QUser("Publisher")
    private fun select(query: SQLQuery<*>): SQLQuery<Report.Companion.ReportBuilder>{
        return query.select(
            constructor(
                Report.Companion.ReportBuilder::class.java,
                report.sample,
                report.service,
                report.createAt,
                report.file,
                report.createAt,
                createBy.id,
                createBy.name,
                report.lastModifyAt,
                modifyBy.id,
                modifyBy.name,
                report.name,
                report.size,
                report.publishAt,
                publishBy.id,
                publishBy.name,
                report.publishLog
            )
        ).from(report)
            .leftJoin(createBy).on(report.createBy.eq(createBy.id))
            .leftJoin(modifyBy).on(report.lastModifyBy.eq(modifyBy.id))
            .leftJoin(publishBy).on(report.publishBy.eq(publishBy.id))
    }
    fun findBySampleAndService(sample: Long, service: String): Flux<Report>{
        return repo.query{
            select(it).where(report.sample.eq(sample).and(report.service.eq(service)))
        }.all().map(Report.Companion.ReportBuilder::build)
    }
}