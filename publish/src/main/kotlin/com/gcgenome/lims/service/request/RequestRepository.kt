package com.gcgenome.lims.service.request

import com.gcgenome.lims.entity.Request
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestRepository: QuerydslR2dbcRepository<Request, Request.Companion.RequestPK>
