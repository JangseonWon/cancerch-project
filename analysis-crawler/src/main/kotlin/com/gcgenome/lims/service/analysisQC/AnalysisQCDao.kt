package com.gcgenome.lims.service.analysisQC

import com.gcgenome.lims.entity.AnalysisQC
import com.gcgenome.querydsl.persist
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AnalysisQCDao(private val repo: AnalysisQCRepository) {
    fun merge(entity: AnalysisQC): Mono<*> = repo.persist(entity)
}