package com.gcgenome.lims.service.preprocessing

import com.gcgenome.lims.data.Preprocessing
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PreprocessingHandler(val dao: PreprocessingDao) {
    @Transactional
    fun save(dto : Preprocessing): Mono<Any> {
        return dao.merge(dto.worklist, dto.index, dto)
    }
    fun saveMany(dtos: List<Preprocessing>) : Flux<Any> {
        return Flux.mergeSequential(dtos.map(this::save))
    }
}