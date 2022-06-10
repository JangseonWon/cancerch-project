package com.greencross.lims.repo

import com.greencross.lims.entity.readonly.Patient
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository

interface PatientRepository : QuerydslR2dbcRepository<Patient, String> {
}