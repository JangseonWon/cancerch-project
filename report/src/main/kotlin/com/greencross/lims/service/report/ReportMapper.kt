package com.greencross.lims.service.report

import com.greencross.lims.entity.Report
import org.springframework.stereotype.Component

@Component
class ReportMapper {
    fun toDto(entity: Report) : com.greencross.lims.data.Report{
        return com.greencross.lims.data.Report(entity.sample, entity.service, entity.createAt.toString()).apply{
            this.createBy = entity.createBy
            this.name = entity.name
        }
    }
}