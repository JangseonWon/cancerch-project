package com.greencross.lims.service

import com.greencross.lims.entity.readonly.Patient
import com.greencross.lims.entity.readonly.QPatient.patient
import com.greencross.lims.entity.readonly.QSample.sample
import com.greencross.lims.repo.PatientRepository
import com.querydsl.sql.SQLQuery
import reactor.core.publisher.Mono

class PatientDao(private val repo: PatientRepository) {
    private fun select(query: SQLQuery<*>): SQLQuery<Patient>{
        return query.select(
            repo.entityProjection()
        ).from(patient)
            .join(sample).on(sample.patient.eq(patient.id_SET))
    }

    fun findBySample(_sample: Long) : Mono<Patient> {
        return repo.query{
            select(it).where(sample.id.eq(_sample))
        }.one()
    }
}