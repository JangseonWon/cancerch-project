package com.gcgenome.lims.service.worklist

import com.gcgenome.lims.data.Worklist
import org.springframework.stereotype.Component

@Component
class WorklistMapper {
    fun toDto(entity: com.gcgenome.lims.entity.Worklist): Worklist {
        return Worklist(entity.id.toString()).apply {
            title = entity.title
            remark = entity.remark
            status = entity.status
            createdAt = entity.createAt.toString()
        }
    }
}