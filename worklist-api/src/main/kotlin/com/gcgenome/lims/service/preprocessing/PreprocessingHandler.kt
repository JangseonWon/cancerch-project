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
        val worklist = dto.id!!.split("$")[0]
        val index = dto.id.split("$")[1]
        return dao.merge(worklist, index, dto.json!!)
    }
    fun saveMany(dtos: List<Preprocessing>) : Flux<Any> {
        return Flux.mergeSequential(dtos.map(this::save))
    }
}