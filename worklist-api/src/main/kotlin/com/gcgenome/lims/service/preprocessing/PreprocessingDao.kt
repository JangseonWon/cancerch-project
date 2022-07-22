package com.gcgenome.lims.service.preprocessing

import com.gcgenome.lims.entity.Preprocessing
import com.gcgenome.lims.entity.QPreprocessing.preprocessing
import com.querydsl.core.types.Projections.constructor
import io.r2dbc.postgresql.codec.Json
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*

@Component
class PreprocessingDao(
    private val repo: PreprocessingRepository,
    private val databaseClient: DatabaseClient
) {
    fun merge(worklist: String, index: Int, json: com.gcgenome.lims.data.Preprocessing): Mono<Any> {
        return repo.query{ it.select(
            constructor(
                Preprocessing::class.java,
                preprocessing.worklist,
                preprocessing.index,
                preprocessing.createBy,
                preprocessing.createAt,
                preprocessing.lastModifyBy,
                preprocessing.lastModifyAt,
                preprocessing.json,
                preprocessing.fragmentSize,
                preprocessing.amount,
                preprocessing.dilution,
                preprocessing.volume,
                preprocessing.libVolume,
                preprocessing.bufferVolume,
                preprocessing.indexI7,
                preprocessing.sequenceI7,
                preprocessing.indexI5,
                preprocessing.sequenceI5
            )
        ).from(preprocessing).where(preprocessing.worklist.eq(worklist).and(preprocessing.index.eq(index)))}
            .one().switchIfEmpty(Mono.just(Preprocessing(worklist, index)))
            .map { it.apply {
                it.fragmentSize = json.fragmentSize
                it.amount = json.amount
                it.dilution = json.dilution
                it.volume = json.volume
                it.libVolume = json.libVolume
                it.bufferVolume = json.bufferVolume
                it.indexI7 = json.indexI7
                it.sequenceI7 = json.sequenceI7
                it.indexI5 = json.indexI5
                it.sequenceI5 = json.sequenceI5
            }}.flatMap { save(it) }
    }

    fun save(entity: Preprocessing) : Mono<Void> {
        if(entity.isNew) {
            val query= "INSERT INTO PREPROCESSING(worklist, index, create_at, create_by, last_modify_at, last_modify_by, value) values(:worklist, :index, :createdAt, :createdBy, :lastModifyAt, :lastModifyBy, :value)"
            return getUser().flatMap{
                databaseClient.sql(query)
                    .bind("worklist", UUID.fromString(entity.worklist))
                    .bind("index", entity.index)
                    .bind("createdAt", LocalDateTime.now())
                    .bind("createdBy",  it.authentication.principal)
                    .bind("lastModifyAt", LocalDateTime.now())
                    .bind("lastModifyBy", it.authentication.principal)
                    .bind("value", Json.of(entity.json!!))
                    .then()
            }.then()
        } else {
            return repo.update {
                it.set(preprocessing.fragmentSize, entity.fragmentSize)
                    .set(preprocessing.amount, entity.amount)
                    .set(preprocessing.dilution, entity.dilution)
                    .set(preprocessing.volume, entity.volume)
                    .set(preprocessing.libVolume, entity.libVolume)
                    .where(preprocessing.worklist.eq(entity.worklist).and(preprocessing.index.eq(entity.index)))
            }.then(Mono.empty())
        }
    }

    private fun getUser() : Mono<SecurityContext> {
        return ReactiveSecurityContextHolder.getContext()
    }
}