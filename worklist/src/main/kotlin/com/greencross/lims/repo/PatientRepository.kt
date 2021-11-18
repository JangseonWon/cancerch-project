package com.greencross.lims.repo

import com.greencross.lims.entity.Patient
import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface PatientRepository : ReactiveCrudRepository<Patient, String>, ReactiveQuerydslPredicateExecutor<Patient>,
    QuerydslR2dbcFragment<Patient> {
        @Query("SELECT id, name, code, TEXT(sex) as sex, customer_name, customer_code, mrn FROM Patient where id=:id")
        override fun findById(id: String): Mono<Patient>
}