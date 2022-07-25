package com.gcgenome.lims.service.worklist

import com.gcgenome.lims.data.Worklist
import com.gcgenome.lims.search.PageReactive
import com.gcgenome.lims.search.SearchParam
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
@Transactional(readOnly = true)
class WorklistHandler(
    private val dao: WorklistDao,
    private val mapper: WorklistMapper
) {
    fun search(query: SearchParam): Mono<PageReactive<Worklist>> = dao.search(query).map { it.map(mapper::toDto) }
    fun current(): Mono<String> {
        return null
    }
    fun max(prefix: String): Mono<Int> {
        return null
    }
}