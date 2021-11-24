package com.greencross.lims.repo

import com.greencross.lims.entity.User
import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor
import org.springframework.data.repository.reactive.ReactiveSortingRepository

interface UserRepository : ReactiveSortingRepository<User, String>, ReactiveQuerydslPredicateExecutor<User>, QuerydslR2dbcFragment<User>