package com.gcgenome.lims.service.preprocessing

import com.gcgenome.lims.entity.Preprocessing
import com.gcgenome.querydsl.PersistQuerydslR2dbcRepo
import org.springframework.stereotype.Repository

@Repository
interface PreprocessingRepository: PersistQuerydslR2dbcRepo<Preprocessing, Preprocessing.Companion.PreprocessingPK>