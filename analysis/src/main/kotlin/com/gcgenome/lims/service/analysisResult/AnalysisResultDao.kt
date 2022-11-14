package com.gcgenome.lims.service.analysisResult

import com.gcgenome.lims.entity.AnalysisResult
import com.gcgenome.querydsl.persist
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class AnalysisResultDao(
    private val repo: AnalysisResultRepository
) {
    fun update(entity: AnalysisResult): Mono<AnalysisResult>{
        return repo.persist(entity)
    }
    fun findBySampleAndService(sample: Long, service: String, batch: String, row: Int): Flux<AnalysisResult> {
        return repo.findBySampleAndServiceAndBatchAndRow(sample, service, batch, row)
    }
}