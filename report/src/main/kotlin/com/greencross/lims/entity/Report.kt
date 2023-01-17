package com.greencross.lims.entity

import com.gcgenome.querydsl.Version
import com.gcgenome.querydsl.Versioned
import com.infobip.spring.data.jdbc.annotation.processor.Schema
import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Schema("avoid")
@Table("report")
@Versioned
data class Report(
    @Column("sample")                 val sample:        Long,
    @Column("service")                val service:       String,
    @Column("create_at")              val createAt:      LocalDateTime
): Persistable<Report.Companion.ReportPK> {
    @Column("file")                   var file:          UUID? = null
    @Column("batch")                  var batch:         String = ""
    @Column("row")                    var row:           Long = 0
    @CreatedBy
    @Column("create_by")     lateinit var createBy:      String
    @LastModifiedDate
    @Column("last_modify_at")lateinit var lastModifyAt:  LocalDateTime
    @LastModifiedBy
    @Column("last_modify_by")lateinit var lastModifyBy:  String
    @Column("name")                   var name:          String=""
    @Column("size")                   var size:          Long? = null
    @Column("publish_at")             var publishAt:     LocalDateTime? = null
    @Column("publish_by")             var publishBy:     String? = null
    @Column("publish_log")            var publishLog:    Json? = null
    @Column("is_printed")             var isPrinted:     String?=null
    @Column("language")               var language:      String?=null
    @Column("description")            var description:   String=""
    @Version @Column("version")       var version:       Long = 0
    @Id @Transient           lateinit var _id:           ReportPK

    companion object{
        data class ReportPK(
            val sample:     Long,
            val service:    String,
            val createAt:   LocalDateTime
        )
    }

    override fun getId(): ReportPK {
        return ReportPK(sample, service, createAt)
    }

    override fun isNew(): Boolean {
        return this::createBy.isInitialized.not()
    }
}