package com.gcgenome.lims.service.analysis

import com.gcgenome.lims.data.Analysis
import com.gcgenome.lims.search.PageReactive
import com.gcgenome.lims.search.SearchParam
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AnalysisHandler(
    private val dao: AnalysisDao,
    private val mapper: AnalysisMapper
) {
    fun search(query: SearchParam): Mono<PageReactive<Analysis>> = dao.search(query).map { it.map(mapper::toDto) }
}