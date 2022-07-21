package com.gcgenome.lims.service.batch

import com.gcgenome.lims.data.Worklist
import com.gcgenome.lims.entity.Batch
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class BatchHandler {
    @Transactional
    fun save(dto : Worklist): Mono<Any> {
        val worklist = dto.id
        val batch = Batch(UUID.fromString(worklist))
        return Mono.just(batch)
    }
    fun saveMany(dtos: List<Worklist>) : Flux<Any> {
        return Flux.mergeSequential(dtos.map(this::save))
    }
}