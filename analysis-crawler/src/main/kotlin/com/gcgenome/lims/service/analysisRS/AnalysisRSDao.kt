package com.gcgenome.lims.service.analysisRS

import com.gcgenome.lims.entity.AnalysisResult
import com.gcgenome.querydsl.persist
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AnalysisRSDao(private val repo: AnalysisRSRepository) {
    fun merge(entity: AnalysisResult): Mono<*> = repo.persist(entity)
}