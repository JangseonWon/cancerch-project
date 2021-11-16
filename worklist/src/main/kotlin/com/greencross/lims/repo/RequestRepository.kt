package com.greencross.lims.repo

import com.greencross.lims.entity.Request
import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import java.util.*

interface RequestRepository: ReactiveCrudRepository<Request, UUID>, ReactiveQuerydslPredicateExecutor<Request>,
    QuerydslR2dbcFragment<Request> {
        fun findByService(service: String): Flux<Request>
    }