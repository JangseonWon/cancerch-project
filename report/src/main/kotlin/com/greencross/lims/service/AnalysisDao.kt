package com.greencross.lims.service

import com.greencross.lims.entity.QAnalysis.analysis
import com.greencross.lims.entity.readonly.QSample.sample
import com.greencross.lims.entity.readonly.QPatient.patient
import com.greencross.lims.entity.readonly.QRequest.request
import com.greencross.lims.projection.Analysis
import com.greencross.lims.repo.AnalysisRepository
import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQuery
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class AnalysisDao(private val repo: AnalysisRepository) {
    private fun select(query: SQLQuery<*>): SQLQuery<Analysis.Companion.AnalysisBuilder> {
        return query.select(
            constructor(
                Analysis.Companion.AnalysisBuilder::class.java,
                analysis.sample,
                analysis.service,
                analysis.value.`as`("value"),
                request.dateRequest.`as`("dateRequest"),
                request.dateSampling.`as`("dateSampling"),
                request.dateDue.`as`("dateDue"),
                sample.sampleType.`as`("sampleType"),
                sample.barcode,
                patient.id_SET.`as`("patient"),
                patient.name.`as`("patientName"),
                patient.sex,
                patient.birth,
                patient.customerName.`as`("customerName"),
                patient.mrn
            )
        ).from(analysis)
            .leftJoin(sample).on(sample.id.eq(analysis.sample).and(sample.service.eq(analysis.service)))
            .leftJoin(request).on(request.sample.eq(analysis.sample).and(request.service.eq(analysis.service)))
            .leftJoin(patient).on(patient.id.eq(sample.patient))
    }
    fun findById(sample: Long, service: String) : Mono<Analysis> {
        return repo.query{
            select(it).where(analysis.sample.eq(sample).and(analysis.service.eq(service)))
        }.one().map(Analysis.Companion.AnalysisBuilder::build)
    }
//    fun merge(sample: Long, service: String, batch: String, row: Long, json: String): Mono<Any>{
//        return repo.query { it.select(
//            constructor(com.greencross.lims.entity.Analysis::class.java,
//                analysis.sample,
//                analysis.service,
//                analysis.batch,
//                analysis.row,
//                analysis.createAt,
//                analysis.createBy,
//                analysis.lastModifyAt,
//                analysis.lastModifyBy,
//                analysis.file,
//                analysis.value
//            )
//        ).from(analysis).where(analysis.sample.eq(sample).and(analysis.service.eq(service)))}
//            .one().switchIfEmpty(Mono.just(com.greencross.lims.entity.Analysis(sample, service)))
//            .map { it.apply { it.value = Json.of(json) }}
//            .flatMap { repo.save(it) }
//    }
}