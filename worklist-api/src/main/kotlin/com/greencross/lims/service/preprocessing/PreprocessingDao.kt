package com.greencross.lims.service.preprocessing

import com.greencross.lims.entity.Preprocessing
import com.greencross.lims.entity.QPreprocessing.preprocessing
import com.greencross.lims.entity.User
import com.querydsl.core.types.Projections.constructor
import io.r2dbc.postgresql.codec.Json
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*

@Component
class PreprocessingDao(
    private val repo: PreprocessingRepository,
    private val databaseClient: DatabaseClient
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
            .map { it.apply { it.json = json }}
            .flatMap { save(it) }
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
                it.set(preprocessing.json, entity.json)
                    .where(preprocessing.worklist.eq(entity.worklist).and(preprocessing.index.eq(entity.index)))
            }.then(Mono.empty())
        }
    }

    private fun getUser() : Mono<SecurityContext> {
        return ReactiveSecurityContextHolder.getContext()
    }
}