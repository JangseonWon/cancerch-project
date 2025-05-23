package com.gcgenome.lims.service.worklist

import com.gcgenome.lims.data.Worklist
import org.springframework.stereotype.Component

@Component
class WorklistMapper {
    fun toDto(entity: com.gcgenome.lims.entity.Worklist): Worklist {
        return Worklist(
            id=entity.id.toString()
        ).apply {
            title    = entity.title
            createAt = entity.createAt.toString()
            createBy = entity.createBy
            status   = entity.status
            remark   = entity.remark ?: ""
            domain   = entity.domain
            serial   = entity.serial ?: "CREATE"
            prefix   = entity.prefix ?: ""
            idx      = entity.idx ?: -999999
            lastModifyAt = (entity.lastModifyAt?: "").toString()
            serializeBy = entity.serializeBy ?: ""
        }
    }
}
