package com.greencross.lims.repo

import com.greencross.lims.entity.Worklist
import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface WorklistRepository: ReactiveCrudRepository<Worklist, String>, ReactiveQuerydslPredicateExecutor<Worklist>, QuerydslR2dbcFragment<Worklist> {

}