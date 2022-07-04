package com.greencross.lims.service

import com.greencross.lims.entity.readonly.QUser
import com.greencross.lims.entity.QReport.report
import com.greencross.lims.projection.Report
import com.greencross.lims.repo.ReportRepository
import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQuery
import io.r2dbc.postgresql.codec.Json
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

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
                report.createAt,
                createBy.id.`as`("creatorId"),
                createBy.name.`as`("creator"),
                report.lastModifyAt.`as`("lastModifyAt"),
                report.lastModifyBy.`as`("lastModifyId"),
                modifyBy.name.`as`("lastModifier"),
                report.name,
                report.size,
                report.publishAt.`as`("publishAt"),
                report.publishBy.`as`("publisherId"),
                publishBy.name.`as`("publisher"),
                report.publishLog.`as`("publishLog")
            )
        ).from(report)
            .leftJoin(createBy).on(createBy.id.eq(report.createBy))
            .leftJoin(modifyBy).on(createBy.id.eq(report.lastModifyBy))
            .leftJoin(publishBy).on(createBy.id.eq(report.publishBy))
    }
    fun findBySampleAndService(sample: Long, service: String) : Flux<Report> {
        return repo.query{
            select(it).where(report.sample.eq(sample).and(report.service.eq(service)))
        }.all().map(Report.Companion.ReportBuilder::build)
    }
    fun merge(sample: Long, service: String, createdAt: LocalDateTime, json: String) : Mono<Any>{
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
        ).from(report).where()}.one().switchIfEmpty(Mono.just(com.greencross.lims.entity.Report(sample, service, createdAt)))
            .map { it.apply { it.publishLog = Json.of(json) }}
            .flatMap { repo.save(it) }
    }

}