package com.gcgenome.lims.service.sequencing

import com.gcgenome.lims.entity.Sequencing
import com.gcgenome.lims.entity.Worklist
import com.gcgenome.lims.service.lims1.Lims1Api
import com.gcgenome.lims.service.lims1.SequencingRepository
import com.gcgenome.lims.service.lims1.WorklistToBatch
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@Service
class SequencingHandler(val repo: WorklistRepository, val repo2: PreprocessingRepository, val repo3: SequencingRepository, val lims1: Lims1Api) {
    @Transactional
    fun sequencing(worklist: Flux<String>): Mono<Boolean> {
        return repo2.findSequencingMax().flatMap { idx ->
            val seq = AtomicInteger(idx)
            worklist.map(UUID::fromString)
                .collectList().flatMap {
                    validation(it, "PENDING", "HOLDING").flatMap { valid -> if (valid) Mono.just(it) else Mono.error(RuntimeException()) }
                }.flatMapMany { toParam(it) }.sort().map { it.apply {
                    val row = seq.incrementAndGet()
                    it.sequencing.stream().forEach { it.sequencing_ = row }
                } }.collectList()
                .flatMap { param ->
                    lims1.createA(param)
                        .flatMap { updateSequencingIdx(param) }
                        .then(shift(param.stream().map { it.worklist }.toList(), "PENDING", "HOLDING"))
                }
        }
    }
    private fun updateSequencingIdx(param: List<WorklistToBatch.Companion.WorklistToBatchParam>) =
        Flux.fromIterable(param).flatMap { param->
            Flux.fromIterable(param.sequencing).flatMap { seq->
                repo2.updatePreprocessingSequencingIdx(seq.worklist, seq.index, seq.sequencing_)
            }
        }.collectList()
    @Transactional(readOnly = true)
    fun sequencingB(worklist: Flux<String>): Mono<Boolean> =  worklist.map(UUID::fromString)
        .collectList().flatMap {
            validation(it, "PENDING", "HOLDING").flatMap { valid -> if (valid) Mono.just(it) else Mono.error(RuntimeException()) }
        }.flatMapMany { toParam(it) }.sort().collectList()
        .flatMap{ param ->
            lims1.createA(param)
                .then(shift(param.stream().map { it.worklist }.toList(), "PENDING_B", "HOLDING_B"))
        }

    private fun validation(worklist:List<UUID>, vararg states: String): Mono<Boolean> {
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

    private fun toParam(worklists: List<UUID>): Flux<WorklistToBatch.Companion.WorklistToBatchParam> =
        Flux.fromIterable(worklists).flatMap {
            val findWorklist: Mono<Worklist> = repo.findById(it)
            val findSequencing: Mono<List<Sequencing>> = repo3.findAllByWorklist(it)
                .sort(Comparator.comparing(Sequencing::index))
                .filter(Sequencing::qc)
                .filter{ it.state.startsWith("PENDING") }
                .collectList()
            Mono.zip(findWorklist, findSequencing)
        }.map { WorklistToBatch.Companion.WorklistToBatchParam(it.t1, it.t2) }

}