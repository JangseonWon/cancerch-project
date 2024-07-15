package com.gcgenome.lims.service.report

import com.gcgenome.lims.entity.QReport.report
import com.gcgenome.lims.entity.QUser
import com.gcgenome.lims.projection.Report
import com.querydsl.core.types.Ops
import com.querydsl.core.types.Projections.constructor
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.sql.SQLQuery
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
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
                report.resultInfo.`as`("resultInfo")
            )
        ).from(report)
            .leftJoin(createBy).on(createBy.id.eq(report.createBy))
            .leftJoin(modifyBy).on(modifyBy.id.eq(report.lastModifyBy))
            .leftJoin(publishBy).on(publishBy.id.eq(report.publishBy))
    }
    fun findForCassandraReport(sample: Long, service: String, createdAt: LocalDateTime): Mono<Report>{
        return repo.query{
            select(it).where(report.sample.eq(sample).and(report.service.eq(service)).and(Expressions.predicate(Ops.EQ, report.createAt, Expressions.asDateTime(createdAt.toString().replace("T", " ")))))
        }.one().map(Report.Companion.ReportBuilder::build)
    }
    fun merge(sample: Long, service: String, createdAt: LocalDateTime) : Mono<Any>{
        return repo.query { it.select(
            constructor(
                com.gcgenome.lims.entity.Report::class.java,
                report.sample,
                report.service,
                report.createAt,
                report.file,
                report.createBy,
                report.lastModifyAt,
                report.lastModifyBy,
                report.name,
                report.size,
                report.publishAt,
                report.publishBy,
                report.publishLog
            )
        ).from(report).where(report.sample.eq(sample).and(report.service.eq(service)).and(Expressions.predicate(Ops.EQ, report.createAt, Expressions.asDateTime(createdAt.toString().replace("T", " ")))))
        }.one().switchIfEmpty(Mono.just(com.gcgenome.lims.entity.Report(sample, service, createdAt)))
            .zipWith(ReactiveSecurityContextHolder.getContext())
            .flatMap { repo.merge(it.t1, it.t2.authentication.principal.toString()) }
    }
    private fun ReportRepository.merge(entity: com.gcgenome.lims.entity.Report, user : String): Mono<com.gcgenome.lims.entity.Report>{
        val now = LocalDateTime.now()
        return if(entity.isNew) repo.save(entity).then(Mono.just(entity))
        else update {
            Expressions.stringPath(report.publishLog.metadata)
            it.set(report.publishAt, now)
            .set(report.publishBy, user)
            .set(report.lastModifyAt, now)
            .set(report.lastModifyBy, user)
                .where(report.sample.eq(entity.sample).and(report.service.eq(entity.service)).and(Expressions.predicate(Ops.EQ, report.createAt, Expressions.asDateTime(entity.createAt.toString().replace("T", " ")))))
        }.then(Mono.just(entity))
    }
}
