package com.greencross.lims.service.report

import com.greencross.lims.entity.readonly.QUser
import com.greencross.lims.entity.QReport.report
import com.greencross.lims.projection.Report
import com.gcgenome.querydsl.persist
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
                report.publishLog.`as`("publishLog"),
                report.isPrinted,
                report.language
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
        return repo.query{
            select(it).where(report.sample.eq(sample).and(report.service.eq(service)).and(report.createAt.stringValue().eq(createdAt.toString().replace("T", " "))))
        }.one().map(Report.Companion.ReportBuilder::build)
    }
    fun create(new: com.greencross.lims.entity.Report): Mono<com.greencross.lims.entity.Report> {
        return repo.save(new)
    }
    fun merge(entity: com.greencross.lims.entity.Report): Mono<Void> {
        return repo.persist(entity).then(Mono.empty())
    }
    fun findReport():Flux<com.greencross.lims.entity.Report> {
        return repo.findTop5ByIsPrinted("PREPARE")
    }
    fun findRequestQueue(): Flux<com.greencross.lims.entity.Report> {
        return repo.findAllByIsPrinted("PREPARE")
    }

}