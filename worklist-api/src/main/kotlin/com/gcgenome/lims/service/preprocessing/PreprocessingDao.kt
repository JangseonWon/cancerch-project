package com.gcgenome.lims.service.preprocessing

import com.gcgenome.lims.entity.Preprocessing
import com.gcgenome.querydsl.persist
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class PreprocessingDao(private val repo: PreprocessingRepository) {
    fun merge(worklist: String, index: Int, dto: com.gcgenome.lims.data.Preprocessing): Mono<*> {
        val entity = Preprocessing(worklist, index).apply {
            concNa       = dto.concNa
            concInput    = dto.concInput
            libPrep      = dto.libPrep
            libConcTape  = dto.libConcTape
            libConcQubit = dto.libConcQubit
            fragmentSize = dto.fragmentSize
            amount       = dto.amount
            dilution     = dto.dilution
            volume       = dto.volume
            libVolume    = dto.libVolume
            bufferVolume = dto.bufferVolume
            indexI7      = dto.indexI7
            sequenceI7   = dto.sequenceI7
            indexI5      = dto.indexI5
            sequenceI5   = dto.sequenceI5
        }
        return repo.persist(entity)
    }
}