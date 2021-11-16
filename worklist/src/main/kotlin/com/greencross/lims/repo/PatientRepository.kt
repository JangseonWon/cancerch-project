package com.greencross.lims.repo

import com.greencross.lims.entity.Patient
import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PatientRepository : ReactiveCrudRepository<Patient, String>, ReactiveQuerydslPredicateExecutor<Patient>,
    QuerydslR2dbcFragment<Patient> {
}