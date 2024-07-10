package com.gcgenome.lims.service.analysisQC

import com.gcgenome.lims.entity.AnalysisQC
import com.gcgenome.lims.entity.QAnalysisQC.analysisQC
import com.gcgenome.querydsl.persist
import com.querydsl.core.types.dsl.Wildcard
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AnalysisQCDao(
    private val repo: AnalysisQCRepository
) {
    fun insert(entity: AnalysisQC): Mono<AnalysisQC> {
        return repo.persist(entity)
    }
    fun findByCompositeKey(sample: Long, service: String, batch: String, row: Int): Mono<AnalysisQC> {
        return repo.findBySampleAndServiceAndBatchAndRow(sample, service, batch, row)
    }
    fun isExistQCData(sample: Long, service: String, batch: String, row: Int): Mono<Boolean> {
        return repo.query {
            it.select(Wildcard.count).from(analysisQC)
                .where(analysisQC.sample.eq(sample)
                    .and(analysisQC.service.eq(service))
                    .and(analysisQC.batch.eq(batch))
                    .and(analysisQC.row.eq(row)))
        }.one().map { it != 0L }
    }
}
