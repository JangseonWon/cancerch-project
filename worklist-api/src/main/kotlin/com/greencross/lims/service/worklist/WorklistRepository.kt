package com.greencross.lims.service.worklist

import com.greencross.lims.entity.Worklist
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import java.util.*

interface WorklistRepository: QuerydslR2dbcRepository<Worklist, UUID>
