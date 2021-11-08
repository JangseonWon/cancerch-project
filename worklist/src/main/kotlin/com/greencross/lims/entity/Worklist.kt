package com.greencross.lims.entity

import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable

import java.time.LocalDateTime
import java.util.*

@Table("worklist")
data class Worklist(
    @Id
    @Column("id") private val id: UUID,
    @Column("no") var no: Int,
    @Column("title") var title: String,
    @CreatedBy
    @Column("created_by")
    var createdBy: String? = null,

    @Column("sample") var sample: Long,
    @Column("state") var state: String,
    @Column("comment") var comment: String,
    @CreatedDate
    @Column("created_at")
    var createdAt: LocalDateTime? = null,
    @Column("activation")
    var activation: String

    ): Serializable, Persistable<UUID>{

    override fun getId(): UUID {
        return id
    }
    override fun isNew(): Boolean {
        return createdAt == null
    }

}