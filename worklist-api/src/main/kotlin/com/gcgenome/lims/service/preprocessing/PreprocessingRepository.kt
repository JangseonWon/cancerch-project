package com.gcgenome.lims.service.preprocessing

import com.gcgenome.lims.entity.Preprocessing
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface PreprocessingRepository: QuerydslR2dbcRepository<Preprocessing, Preprocessing.Companion.PreprocessingPK> {

}