package com.gcgenome.lims.service.sequencing

import com.gcgenome.lims.entity.Worklist
import com.gcgenome.lims.service.lims1.Lims1Api
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
@Transactional(readOnly = true)
class SequencingHandler(val repo: WorklistRepository, val repo2: PreprocessingRepository, val lims1: Lims1Api) {
    fun sequencing(worklist: Flux<String>): Mono<Boolean> = worklist.map(UUID::fromString)
        .collectList().flatMap {
            validation(it, "PENDING", "HOLDING").flatMap { valid -> if(valid) Mono.just(it) else Mono.error(RuntimeException()) }
        }.flatMapMany(repo::findAllById)
        .collectList().flatMap{ lims1.create(it).then(shift(it, "PENDING", "HOLDING")) }
    fun sequencingB(worklist: Flux<String>): Mono<Boolean> = worklist.map(UUID::fromString)
        .collectList().flatMap {
            validation(it, "PENDING_B", "HOLDING_B").flatMap { valid -> if(valid) Mono.just(it) else Mono.error(RuntimeException()) }
        }.flatMapMany(repo::findAllById)
        .collectList().flatMap{ lims1.createB(it).then(shift(it, "PENDING_B", "HOLDING_B")) }
    private fun validation(worklists:List<UUID>, vararg states: String): Mono<Boolean> {
        // worklists의 Preprocessing.state가 states 안에 있는지 확인
        // 단순 쿼리로 처리해도 무방
        return Mono.just(true)
    }
    private fun shift(worklists:List<Worklist>, vararg states: String): Mono<Boolean> =
        Flux.fromIterable(worklists).flatMap {
            Flux.fromArray(states).flatMap{ state ->
                repo2.shiftPreprocessingState(it.id, state, shift(state))
            }
        }.last().then(Mono.just(true))

    private fun shift(state: String): String = when(state) {
        "PENDING" -> "PENDING_B"
        "HOLDING" -> "HOLDING_B"
        "PENDING_B" -> "COMPLETE"
        "HOLDING_B" -> "PENDING"
        else -> throw RuntimeException("")
    }
}