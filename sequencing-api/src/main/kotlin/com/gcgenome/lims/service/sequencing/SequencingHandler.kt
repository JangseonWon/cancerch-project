package com.gcgenome.lims.service.sequencing

import com.gcgenome.lims.service.lims1.Lims1Api
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
@Transactional(readOnly = true)
class SequencingHandler(val repo: WorklistRepository, val lims1: Lims1Api) {
    fun sequencing(worklist: Flux<String>): Mono<Void> = worklist.map(UUID::fromString)
        .collectList().flatMapMany(repo::findAllById)
        .collectList().map(lims1::create).then()
}