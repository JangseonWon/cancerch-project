package com.greencross.lims.entity

import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

import java.io.Serializable
import java.time.LocalDateTime

@Table("RnD.worklist")
data class Worklist(
    @Id
    @Column("id") private val id: String
    ) : Serializable, Persistable <String> {
    @Column("no") var no: String? = null
    @Column("title") var title: String? = null

    @CreatedBy
    @Column("created_by")
    var createdBy: String? = null

    @Column("sample") var sample: Long? = null
    @Column("state") var state: String? = null
    @Column("comment") var comment: String? = null

    @CreatedDate
    @Column("created_at")
    var createdAt: LocalDateTime? = null


    override fun getId(): String {
        return id
    }
    override fun isNew(): Boolean {
        return createdAt == null
    }
}