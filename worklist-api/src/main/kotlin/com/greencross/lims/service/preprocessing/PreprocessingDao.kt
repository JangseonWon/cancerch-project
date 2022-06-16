package com.greencross.lims.service.preprocessing

import com.greencross.lims.entity.Preprocessing
import com.greencross.lims.entity.QPreprocessing.preprocessing
import com.querydsl.core.types.Projections.constructor
import io.r2dbc.postgresql.codec.Json
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class PreprocessingDao(
    private val repo: PreprocessingRepository
) {
    fun merge(worklist: String, index: String, json: String): Mono<Any> {
        return repo.query{ it.select(
            constructor(Preprocessing::class.java,
                preprocessing.worklist,
                preprocessing.index,
                preprocessing.createBy,
                preprocessing.createAt,
                preprocessing.lastModifyBy,
                preprocessing.lastModifyAt,
                preprocessing.json
            )
        ).from(preprocessing).where(preprocessing.worklist.eq(worklist).and(preprocessing.index.eq(index.toInt())))}
            .one().switchIfEmpty(Mono.just(Preprocessing(worklist, index.toInt())))
            .map { it.apply { it.json = Json.of(json) }}
            .flatMap { repo.save(it) }
    }
}