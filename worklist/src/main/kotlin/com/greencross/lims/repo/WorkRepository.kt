package com.greencross.lims.repo

import com.greencross.lims.entity.Work
import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface WorkRepository: ReactiveCrudRepository<Work, String>, ReactiveQuerydslPredicateExecutor<Work>,
    QuerydslR2dbcFragment<Work> {
    fun findByWorklist(worklist: String): Flux<Work>
}