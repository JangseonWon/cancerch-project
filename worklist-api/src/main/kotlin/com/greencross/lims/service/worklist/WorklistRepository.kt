package com.greencross.lims.service.worklist

import com.greencross.lims.entity.Worklist
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WorklistRepository: QuerydslR2dbcRepository<Worklist, UUID>
