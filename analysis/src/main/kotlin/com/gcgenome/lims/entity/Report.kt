package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Schema("avoid")
@Table("report")
data class Report(
    @Column("sample")                    val sample:        Long,
    @Column("service")                   val service:       String,
    @CreatedDate
    @Column("create_at")                 val createAt:      LocalDateTime
) : Persistable<Report.Companion.ReportPK> {
    @Column("file")             lateinit var file:          UUID
    @CreatedBy
    @Column("create_by")        lateinit var createBy:      String
    @LastModifiedDate
    @Column("last_modify_at")   lateinit var lastModifyAt:  LocalDateTime
    @LastModifiedBy
    @Column("last_modify_by")   lateinit var lastModifyBy:  String
    @Column("name")             lateinit var name:          String
    @Column("size")                      var size:          Int? = 0
    @Column("publish_at")       lateinit var publishAt:     LocalDateTime
    @Column("publish_by")       lateinit var publishBy:     String
    @Column("publish_log")      lateinit var publishLog:    String
    @Id @Transient              lateinit var _id:           ReportPK

    companion object{
        data class ReportPK(
            val sample: Long,
            var service: String,
            var createAt: LocalDateTime
        )
    }

    override fun getId(): ReportPK {
        return ReportPK(sample, service, createAt)
    }

    override fun isNew(): Boolean {
        return true
    }
}