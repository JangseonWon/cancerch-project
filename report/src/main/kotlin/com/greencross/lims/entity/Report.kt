package com.greencross.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Schema("avoid")
@Table("report")
data class Report(
    @Column("sample")                 val sample:        Long,
    @Column("service")                val service:       String,
    @Column("create_at")              val createAt:      LocalDateTime
): Persistable<Report.Companion.ReportPK> {
    @Column("file")          lateinit var file:          UUID
    @CreatedBy
    @Column("create_by")     lateinit var createBy:      String
    @LastModifiedDate
    @Column("last_modify_at")lateinit var lastModifyAt:  LocalDateTime
    @LastModifiedBy
    @Column("last_modify_by")lateinit var lastModifyBy:  String
    @Column("name")          lateinit var name:          String
    @Column("size")                   var size:          Long? = null
    @Column("publish_at")             var publishAt:     LocalDateTime? = null
    @Column("publish_by")             var publishBy:     String? = null
    @Column("publish_log")            var publishLog:    Json? = null

    @Id @Transient   lateinit var _id: ReportPK
    constructor(
        sample: Long,
        service: String,
        createAt: LocalDateTime,
        file: UUID,
        createBy: String,
        lastModifyAt: LocalDateTime,
        lastModifyBy: String,
        name: String,
        size: Long,
        publishAt: LocalDateTime,
        publishBy: String,
        publishLog: Json
    ) : this(sample, service, createAt) {
        this.file = file
        this.createBy = createBy
        this.lastModifyBy = lastModifyBy
        this.lastModifyAt = lastModifyAt
        this.name = name
        this.size = size
        this.publishAt = publishAt
        this.publishBy = publishBy
        this.publishLog = publishLog
    }

    companion object{
        data class ReportPK(
            val sample: Long,
            val service: String
        )
    }

    override fun getId(): ReportPK {
        return ReportPK(sample, service)
    }

    override fun isNew(): Boolean {
        return this::createBy.isInitialized.not()
    }
}