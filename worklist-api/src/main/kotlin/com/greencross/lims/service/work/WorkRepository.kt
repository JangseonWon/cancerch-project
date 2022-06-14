package com.greencross.lims.service.work

import com.greencross.lims.entity.Work
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface WorkRepository: QuerydslR2dbcRepository<Work, Work.Companion.WorkPK>