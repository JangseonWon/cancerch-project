package com.gcgenome.lims.service.index

import com.gcgenome.lims.entity.Index
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface IndexRepository: QuerydslR2dbcRepository<Index, Index.Companion.IndexPK>
