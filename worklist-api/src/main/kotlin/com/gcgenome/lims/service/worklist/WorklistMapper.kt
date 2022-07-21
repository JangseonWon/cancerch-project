package com.gcgenome.lims.service.worklist

import com.gcgenome.lims.data.Worklist
import org.springframework.stereotype.Component

@Component
class WorklistMapper {
    fun toDto(entity: com.gcgenome.lims.entity.Worklist): Worklist {
        return Worklist(
            id=entity.id.toString(),
            title= if(entity.title!=null) entity.title!! else "",
            createdAt = entity.createAt.toString(),
            status = entity.status,
            prefix = entity.prefix,
            idx = entity.idx,
            serial = entity.serial
        ).apply {
            remark = entity.remark
        }
    }
}