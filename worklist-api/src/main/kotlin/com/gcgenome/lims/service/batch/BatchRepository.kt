package com.gcgenome.lims.service.batch

import com.gcgenome.lims.entity.Batch
import com.gcgenome.querydsl.PersistQuerydslR2dbcRepo
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BatchRepository : PersistQuerydslR2dbcRepo<Batch, UUID> {
}