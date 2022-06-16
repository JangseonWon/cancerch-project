package com.greencross.lims.service.preprocessing

import com.greencross.lims.entity.Preprocessing
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface PreprocessingRepository: QuerydslR2dbcRepository<Preprocessing, Preprocessing.Companion.PreprocessingPK> {
}