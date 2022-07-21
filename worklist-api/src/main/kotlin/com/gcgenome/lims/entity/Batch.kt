package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.util.*

@Schema("avoid")
@Table("batch")
data class Batch (
    @Id val id: UUID
) : Serializable {
    var prefix: String? = null
        set(value) {
            field = value
            serial = field+idx
        }
    var idx: Int? = null
        set(value) {
            field = value
            serial = prefix+field
        }
    var serial:    String? = prefix+idx
    operator fun String?.plus(other: Int?): String? {
        return if(this == null || other == null) null
        else this+other
    }
}