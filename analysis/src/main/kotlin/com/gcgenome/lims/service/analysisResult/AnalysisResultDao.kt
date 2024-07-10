package com.gcgenome.lims.service.analysisResult

import com.gcgenome.lims.entity.AnalysisResult
import com.gcgenome.lims.entity.QAnalysisResult.analysisResult
import com.gcgenome.querydsl.persist
import com.querydsl.core.types.dsl.Wildcard
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AnalysisResultDao(
    private val repo: AnalysisResultRepository
) {
    fun insertAndUpdate(entity: AnalysisResult): Mono<AnalysisResult>{
        return repo.persist(entity)
    }
    fun findByCompositeKey(sample: Long, service: String, batch: String, row: Int): Mono<AnalysisResult> {
        return repo.findBySampleAndServiceAndBatchAndRow(sample, service, batch, row)
    }
    fun checkExistResultData(sample: Long, service: String, batch: String, row: Int): Mono<Boolean> {
        return repo.query {
            it.select(Wildcard.all).from(analysisResult)
                .where(analysisResult.sample.eq(sample)
                    .and(analysisResult.service.eq(service))
                    .and(analysisResult.batch.eq(batch))
                    .and(analysisResult.row.eq(row)))
        }.all().count().map { it != 0L }
    }
}
