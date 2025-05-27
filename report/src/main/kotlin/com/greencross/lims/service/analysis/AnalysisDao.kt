package com.greencross.lims.service.analysis

import com.greencross.lims.entity.QAnalysis.analysis
import com.greencross.lims.entity.readonly.QSample.sample
import com.greencross.lims.entity.readonly.QPatient.patient
import com.greencross.lims.entity.readonly.QRequest.request
import com.greencross.lims.entity.readonly.QRequestInfo
import com.greencross.lims.entity.readonly.QRequestInfo.requestInfo
import com.greencross.lims.projection.Analysis
import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQuery
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class AnalysisDao(private val repo: AnalysisRepository) {
    private fun select(query: SQLQuery<*>): SQLQuery<Analysis.Companion.AnalysisBuilder> {
        val language = QRequestInfo("language")
        val clinicalCancer = QRequestInfo("clinical_cancer")
        return query.select(
            constructor(
                Analysis.Companion.AnalysisBuilder::class.java,
                analysis.sample,
                analysis.service,
                analysis.batch,
                analysis.row,
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
                analysis.comment,
                analysis.iscore,
                analysis.femsCovBc.`as`("femsCovBc"),
                analysis.femsBc.`as`("femsBc"),
                analysis.covBc.`as`("covBc"),
                analysis.femsCovBernn.`as`("femsCovBernn"),
                analysis.femsPath.`as`("femsPath"),
                analysis.iscorePath.`as`("iscorePath"),
                language.value.`as`("language"),
                clinicalCancer.value.`as`("clinicalCancer"),
                analysis.cfDNAContentration.`as`("cfDnaConcentration")
            )
        ).from(analysis)
            .leftJoin(sample).on(sample.id.eq(analysis.sample))
            .leftJoin(request).on(request.sample.eq(analysis.sample).and(request.service.eq(analysis.service)))
            .leftJoin(patient).on(patient.id_SET.eq(sample.patient))
            .leftJoin(requestInfo).on(
                request.sample.eq(requestInfo.sample).and(request.service.eq(requestInfo.service))
                    .and(requestInfo.code.eq("TA0023"))
            )
            .leftJoin(language).on(
                request.sample.eq(language.sample).and(request.service.eq(language.service))
                    .and(language.code.eq("TA0027"))
            )
            .leftJoin(clinicalCancer).on(
                request.sample.eq(clinicalCancer.sample).and(request.service.eq(clinicalCancer.service))
                    .and(clinicalCancer.code.eq("TA0030"))
            )
    }

    fun findById(sample: Long, service: String, batch: String, row: Long) : Mono<Analysis> {
        return repo.query{
            select(it).where(analysis.sample.eq(sample).and(analysis.service.eq(service)).and(analysis.batch.eq(batch)).and(analysis.row.eq(row)))
        }.one().map(Analysis.Companion.AnalysisBuilder::build)
    }

    fun findOneOrManyBy(entity: Analysis): Mono<List<Analysis>> {
        return if(entity.service == "ON206") repo.query {
            select(it).where(patient.id_SET.eq(entity.patient.id_SET).and(analysis.service.eq(entity.service)
                .and(analysis.sample.loe(entity.sample))))
                .orderBy(analysis.sample.desc()).limit(5)
        }.all().map(Analysis.Companion.AnalysisBuilder::build).collectList()
        else repo.query {
            select(it).where(analysis.sample.eq(entity.sample).and(analysis.service.eq(entity.service)).and(analysis.batch.eq(entity.batch).and(analysis.row.eq(entity.row))))
        }.all().map(Analysis.Companion.AnalysisBuilder::build).collectList()
    }
}
