package com.greencross.lims.repo

import com.greencross.lims.entity.Service_
import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ServiceRepository : ReactiveCrudRepository<Service_, String>, ReactiveQuerydslPredicateExecutor<Service_>,
    QuerydslR2dbcFragment<Service_> {

}