package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Schema("avoid")
@Table("analysis_result")
data class AnalysisResult(
    @Column("sample")                    val sample:         Long,
    @Column("service")                   val service:        String
): Persistable<AnalysisResult.Companion.AnalysisResultPK> {
    @Column("result")                    var result:        String = ""
    @Column("too5_pred")                 var too5Pred:      String = ""
    @Column("too6_pred")                 var too6Pred:      String = ""
    @LastModifiedDate
    @Column("last_modify_at")   lateinit var lastModifyAt:   LocalDateTime
    @LastModifiedBy
    @Column("last_modify_by")   lateinit var lastModifyBy:   String
    @Column("comment")                   var comment:        String? = ""
    @Transient @Id              lateinit var _id:            AnalysisResultPK
    companion object{
        data class AnalysisResultPK(
            val sample: Long,
            val service: String
        )
    }

    override fun getId(): AnalysisResultPK {
        return AnalysisResultPK(sample, service)
    }
    override fun isNew(): Boolean = false
}