package com.gcgenome.lims.service.request

import com.gcgenome.lims.entity.Patient
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PatientRepository: QuerydslR2dbcRepository<Patient, String>
