package com.greencross.lims.service.analysis

import com.greencross.lims.entity.QAnalysis.analysis
import com.greencross.lims.entity.readonly.QSample.sample
import com.greencross.lims.entity.readonly.QPatient.patient
import com.greencross.lims.entity.readonly.QRequest.request
import com.greencross.lims.projection.Analysis
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
                patient.mrn,
                analysis.result,
                analysis.too5Pred.`as`("too5Pred"),
                analysis.too6Pred.`as`("too6Pred")
            )
        ).from(analysis)
            .leftJoin(sample).on(sample.id.eq(analysis.sample))
            .leftJoin(request).on(request.sample.eq(analysis.sample).and(request.service.eq(analysis.service)))
            .leftJoin(patient).on(patient.id_SET.eq(sample.patient))
    }
    fun findById(sample: Long, service: String) : Mono<Analysis> {
        return repo.query{
            select(it).where(analysis.sample.eq(sample).and(analysis.service.eq(service)))
        }.one().map(Analysis.Companion.AnalysisBuilder::build)
    }
}