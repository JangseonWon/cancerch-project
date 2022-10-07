package com.gcgenome.lims.service.sequencing

import com.gcgenome.lims.entity.Preprocessing
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
interface PreprocessingRepository: QuerydslR2dbcRepository<Preprocessing, Preprocessing.Companion.PreprocessingPK> {
    fun findAllByWorklistIn(worklists: List<UUID>): Flux<Preprocessing>
}
