package com.gcgenome.lims.service.batch

import com.gcgenome.lims.data.Worklist
import com.gcgenome.lims.entity.Batch
import com.gcgenome.querydsl.persist
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class BatchHandler(private val repo: BatchRepository) {
    @Transactional
    fun save(dto : Worklist): Mono<Batch> {
        val worklist = dto.id
        val batch = Batch(UUID.fromString(worklist)).apply{
            prefix = dto.prefix
            idx = dto.idx
            serial = dto.prefix+String.format("%03d", dto.idx)
        }
        return repo.save(batch)
    }
    fun saveMany(dtos: List<Worklist>) : Flux<Any> {
        return Flux.mergeSequential(dtos.map(this::save))
    }
}