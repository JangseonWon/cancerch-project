package com.gcgenome.lims.service.request

import com.gcgenome.lims.data.Sample
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SampleRepository: QuerydslR2dbcRepository<Sample, Long> {
}
