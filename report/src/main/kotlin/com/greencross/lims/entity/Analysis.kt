package com.greencross.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Schema("avoid")
@Table("analysis")
data class Analysis(
    @Column("sample")           val sample:         Long,
    @Column("service")          val service:        String
): Persistable<Analysis.Companion.AnalysisPK> {
    @Column("batch")            lateinit var batch:          String
    @Column("row")                       var row:            Long? = null
    @Column("create_at")        lateinit var createAt:       LocalDateTime
    @Column("create_by")        lateinit var createBy:       String
    @Column("last_modify_at")   lateinit var lastModifyAt:   LocalDateTime
    @Column("last_modify_by")   lateinit var lastModifyBy:   String
    @Column("file")             lateinit var file:           String
    @Column("result")           lateinit var result:         String
    @Column("too5_pred")        lateinit var cancer:         String
    @Id @Transient lateinit var _id: AnalysisPK
    constructor(sample: Long, service: String, batch: String, row: Long, createAt: LocalDateTime, createdBy: String, lastModifiedBy: String, lastModifiedAt: LocalDateTime, file: String, result: String, cancer: String, value: Json) : this(sample, service) {
        this.row = row
        this.batch = batch
        this.createBy = createdBy
        this.createAt = createAt
        this.lastModifyBy = lastModifiedBy
        this.lastModifyAt = lastModifiedAt
        this.file = file
        this.result = result
        this.cancer = cancer
    }
    override fun getId(): AnalysisPK {
        return AnalysisPK(sample, service)
    }
    override fun isNew(): Boolean{
        return false
    }
   companion object {
       data class AnalysisPK(
           val sample: Long,
           val service: String
       )
   }
}