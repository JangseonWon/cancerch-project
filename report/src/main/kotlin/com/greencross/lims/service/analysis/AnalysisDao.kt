package com.greencross.lims.service.analysis

import com.greencross.lims.entity.QAnalysis.analysis
import com.greencross.lims.entity.readonly.QSample.sample
import com.greencross.lims.entity.readonly.QPatient.patient
import com.greencross.lims.entity.readonly.QRequest.request
import com.greencross.lims.entity.readonly.QRequestInfo.requestInfo
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
                sample.remark,
                request.ward,
                requestInfo.code,
                requestInfo.value,
                patient.id_SET.`as`("patient"),
                patient.name.`as`("patientName"),
                patient.sex,
                patient.birth,
                patient.customerName.`as`("customerName"),
                patient.customerName2.`as`("customerName2"),
                patient.mrn,
                analysis.result,
                analysis.too5Pred.`as`("too5Pred"),
                analysis.too6Pred.`as`("too6Pred"),
                analysis.comment
            )
        ).from(analysis)
            .leftJoin(sample).on(sample.id.eq(analysis.sample))
            .leftJoin(request).on(request.sample.eq(analysis.sample).and(request.service.eq(analysis.service)))
            .leftJoin(patient).on(patient.id_SET.eq(sample.patient))
            .leftJoin(requestInfo).on(request.sample.eq(requestInfo.sample).and(request.service.eq(requestInfo.service)).and(requestInfo.code.eq("TA0023")))
    }
    fun findById(sample: Long, service: String, batch: String, row: Long) : Mono<Analysis> {
        return repo.query{
            select(it).where(analysis.sample.eq(sample).and(analysis.service.eq(service)).and(analysis.batch.eq(batch)).and(analysis.row.eq(row)))
        }.one().map(Analysis.Companion.AnalysisBuilder::build)
    }
}
