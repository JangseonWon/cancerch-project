package com.gcgenome.lims.service.sequencing

import com.gcgenome.lims.entity.Preprocessing
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface PreprocessingRepository: QuerydslR2dbcRepository<Preprocessing, Preprocessing.Companion.PreprocessingPK> {
    fun findAllByWorklistIn(worklists: List<UUID>): Flux<Preprocessing>
    @Query("SELECT COALESCE(MAX(sequencing), 0) FROM preprocessing WHERE EXTRACT(YEAR FROM create_at) >= EXTRACT(YEAR FROM CURRENT_DATE)")
    fun findSequencingMax(): Mono<Int>
    @Query("SELECT distinct sequencing FROM preprocessing WHERE worklist IN(:worklists)")
    fun findSequencingByWorklists(worklists: List<UUID>): Mono<Int>
    @Modifying
    @Query("UPDATE preprocessing SET state=:next WHERE worklist=:worklist AND state=:current")
    fun shiftPreprocessingState(worklist:UUID, current: String, next:String): Mono<*>
    @Modifying
    @Query("UPDATE preprocessing SET sequencing=:sequencing WHERE worklist=:worklist AND index=:index")
    fun updatePreprocessingSequencingIdx(worklist:UUID, index: Int, sequencing: Int): Mono<*>
}
