package com.greencross.lims.service.report

import com.greencross.lims.entity.Report
import org.springframework.stereotype.Component

@Component
class ReportMapper {
    fun toDto(entity: Report) : com.greencross.lims.data.Report_{
        return com.greencross.lims.data.Report_(entity.sample, entity.service, entity.createAt.toString()).apply{
            this.createBy = entity.createBy
            this.name = entity.name
        }
    }
    //Entity -> Shared.Report
    fun toMessageDto(entity: Report): com.gcgenome.lims.data.Report{
        return com.gcgenome.lims.data.Report()
            .sample(entity.sample)
            .service(entity.service)
            .createAt(entity.createAt.toString())
            .creator(entity.createBy)
    }
}