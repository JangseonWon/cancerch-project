package com.gcgenome.lims.service.report

import com.gcgenome.SecurityContextRepository
import com.gcgenome.lims.entity.QUser
import com.gcgenome.lims.entity.QReport.report
import com.gcgenome.lims.entity.User
import com.gcgenome.lims.projection.Report

import com.querydsl.core.types.Projections.constructor
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.sql.SQLQuery
import io.r2dbc.postgresql.codec.Json
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
                report.publishLog.`as`("publishLog")
            )
        ).from(report)
            .leftJoin(createBy).on(createBy.id.eq(report.createBy))
            .leftJoin(modifyBy).on(modifyBy.id.eq(report.lastModifyBy))
            .leftJoin(publishBy).on(publishBy.id.eq(report.publishBy))
    }
    fun findForCassandraReport(sample: Long, service: String, createdAt: LocalDateTime): Mono<Report>{
        return repo.query{
            select(it).where(report.sample.eq(sample).and(report.service.eq(service)).and(report.createAt.stringValue().eq(createdAt.toString().replace("T", " "))))
        }.one().map(Report.Companion.ReportBuilder::build)
    }
    fun merge(sample: Long, service: String, createdAt: LocalDateTime, publishLog: String) : Mono<Any>{
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
        ).from(report).where(report.sample.eq(sample).and(report.service.eq(service)).and(report.createAt.stringValue().eq(createdAt.toString().replace("T", " "))))
        }.one().switchIfEmpty(Mono.just(com.gcgenome.lims.entity.Report(sample, service, createdAt)))
            .zipWith(ReactiveSecurityContextHolder.getContext())
            .flatMap { repo.merge(it.t1, publishLog, it.t2.authentication.principal.toString()) }
    }
    private fun ReportRepository.merge(entity: com.gcgenome.lims.entity.Report, json: String?, user : String): Mono<Void>{
        return if(entity.isNew) repo.save(entity.apply { if(json!=null) entity.publishLog = Json.of(json)}).then()
        else update {
            Expressions.stringPath(report.publishLog.metadata)
            if(json!=null) {
                it.set(Expressions.stringPath(report.publishLog.metadata),json)
                    .set(report.publishAt, LocalDateTime.now())
                    .set(report.publishBy, user)
            } else {
                it.setNull(report.publishLog)
            }.where(report.sample.eq(entity.sample).and(report.service.eq(entity.service)).and(report.createAt.stringValue().eq(entity.createAt.toString().replace("T", " "))))
        }.then()
    }
}