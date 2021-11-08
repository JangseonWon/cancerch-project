package com.greencross.lims.repo

import com.greencross.lims.entity.Worklist
import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment
import com.querydsl.core.types.OrderSpecifier
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import java.util.*

interface WorklistRepository: ReactiveCrudRepository<Worklist, UUID>, ReactiveQuerydslPredicateExecutor<Worklist>, QuerydslR2dbcFragment<Worklist> {
//    fun findAllByActivation(orderSpecifier: OrderSpecifier<Int>, activation: String): Flux<Worklist>
}