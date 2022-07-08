package com.greencross.lims.service.report

import com.greencross.lims.entity.readonly.QUser
import com.greencross.lims.entity.QReport.report
import com.greencross.lims.projection.Report
import com.greencross.lims.service.reportfile.ReportFileRepository
import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQuery
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

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
                report.file,
                createBy.id.`as`("createId"),
                report.createAt.`as`("createAt"),
                createBy.name.`as`("createBy"),
                report.lastModifyBy.`as`("lastModifyId"),
                report.lastModifyAt.`as`("lastModifyAt"),
                modifyBy.name.`as`("lastModifyBy"),
                report.name,
                report.size,
                report.publishBy.`as`("publishId"),
                report.publishAt.`as`("publishAt"),
                publishBy.name.`as`("publisher"),
                report.publishLog.`as`("publishLog")
            )
        ).from(report)
            .leftJoin(createBy).on(createBy.id.eq(report.createBy))
            .leftJoin(modifyBy).on(modifyBy.id.eq(report.lastModifyBy))
            .leftJoin(publishBy).on(publishBy.id.eq(report.publishBy))
    }
    fun findBySampleAndService(sample: Long, service: String) : Flux<Report> {
        return repo.query{
            select(it).where(report.sample.eq(sample).and(report.service.eq(service)))
        }.all().map(Report.Companion.ReportBuilder::build)
    }
    fun findForCassandraReport(sample: Long, service: String, createdAt: LocalDateTime): Mono<Report>{
        print(createdAt)
        return repo.query{
            select(it).where(report.sample.eq(sample).and(report.service.eq(service)).and(report.createAt.stringValue().eq(createdAt.toString().replace("T", " "))))
        }.one().map(Report.Companion.ReportBuilder::build)
    }
    fun create(new: com.greencross.lims.entity.Report): Mono<com.greencross.lims.entity.Report> {
        return repo.save(new)
    }
    fun merge(sample: Long, service: String, createdAt: LocalDateTime) : Mono<Any>{
        return repo.query { it.select(
            constructor(
                com.greencross.lims.entity.Report::class.java,
                report.sample,
                report.service,
                report.file,
                report.createAt,
                report.createBy,
                report.lastModifyAt,
                report.lastModifyBy,
                report.name,
                report.size,
                report.publishAt,
                report.publishBy,
                report.publishLog
                )
            ).from(report).where(report.sample.eq(sample).and(report.service.eq(service)))
        }.one().switchIfEmpty(Mono.just(com.greencross.lims.entity.Report(sample, service, createdAt)))
            .map { it.apply {
                it.publishLog = publishLog
            }}
            .flatMap { repo.save(it) }
    }

}