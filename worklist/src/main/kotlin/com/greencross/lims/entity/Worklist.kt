package com.greencross.lims.entity

import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

import java.time.LocalDateTime

@Table("worklist")
data class Worklist(
    @Id
    @Column("id") private val id: String,
    @Column("no") var no: Int,
    @Column("title") var title: String,
    @CreatedBy
    @Column("created_by")
    var createdBy: String,

    @Column("sample") var sample: Long,
    @Column("state") var state: String,
    @Column("comment") var comment: String,

    @CreatedDate
    @Column("created_at")
    var createdAt: LocalDateTime
    )
{

}