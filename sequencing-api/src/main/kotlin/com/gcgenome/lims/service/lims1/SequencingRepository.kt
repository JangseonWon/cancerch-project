package com.gcgenome.lims.service.lims1

import com.gcgenome.lims.entity.Sequencing
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
interface SequencingRepository: QuerydslR2dbcRepository<Sequencing, Sequencing.Companion.SequencingPK> {
    fun findAllByWorklist(worklist: UUID): Flux<Sequencing>
}
