package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Schema("avoid")
@Table("batch")
data class Batch (
    @Column("worklist") @Id val batchId: UUID
) : Serializable, Persistable<UUID> {
    @CreatedDate @Column("create_at")
    lateinit var createAt: LocalDateTime
    @CreatedBy   @Column("create_by")
    lateinit var createBy: String
    @LastModifiedDate @Column("last_modify_at")
    lateinit var lastModifyAt: LocalDateTime
    @LastModifiedBy   @Column("last_modify_by")
    lateinit var lastModifyBy: String

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

    override fun isNew(): Boolean {
        return true
    }

    override fun getId(): UUID {
        return batchId
    }
}