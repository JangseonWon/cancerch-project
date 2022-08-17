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
@Table("analysis_result")
data class Analysis(
    @Column("sample")           val sample:         Long,
    @Column("service")          val service:        String
): Persistable<Analysis.Companion.AnalysisPK> {
    @Column("batch")            lateinit var batch:          String
    @Column("row")                       var row:            Long? = null
    @Column("file")             lateinit var file:           String
    @Column("result")           lateinit var result:         String
    @Column("too5_pred")        lateinit var too5Pred:       String
    @Column("too6_pred")        lateinit var too6Pred:       String
    @Id @Transient lateinit var _id: AnalysisPK
    constructor(sample: Long, service: String, batch: String, row: Long, file: String, result: String, too5Pred: String, too6Pred: String) : this(sample, service) {
        this.row = row
        this.batch = batch
        this.file = file
        this.result = result
        this.too5Pred = too5Pred
        this.too6Pred = too6Pred
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