package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Schema("avoid")
@Table("analysis_qc")
data class AnalysisQC(
    @Column("sample")                 val sample:          Long,
    @Column("service")                val service:         String,
    @Column("batch")                  val batch:           String,
    @Column("row")                    val row:             Int
): Persistable<AnalysisQC.Companion.AnalysisQCPK> {
    @CreatedDate
    @Column("create_at")     lateinit var createAt:        LocalDateTime
    @Column("file")          lateinit var filePath:        String
    @Column("freemix")                var freemix:         Double = 0.0
    @Column("gc")                     var gc:              Double = 0.0
    @Column("raw_reads_millions")     var rawReads:        Double = 0.0
    @Column("dup_rate")               var dupRate:         Double = 0.0
    @Column("total_reads")            var filterReads:     Double = 0.0
    @Column("mean")                   var mean:            Double = 0.0
    @Column("median")                 var median:          Double = 0.0
    @Column("qc")                     var qc:              String = ""
    @Column("freemix_tmp")            var freemixTmp:      Double = 0.0
    @Column("gc_tmp")                 var gcTmp:           Double = 0.0
    @Column("raw_reads_millions_tmp") var rawReadsTmp:     Double = 0.0
    @Column("dup_rate_tmp")           var dupRateTmp:      Double = 0.0
    @Column("total_reads_tmp")        var filterReadsTmp:  Double = 0.0
    @Column("mean_tmp")               var meanTmp:         Double = 0.0
    @Column("median_tmp")             var medianTmp:       Double = 0.0
    @Column("qc_tmp")                 var qcTmp:           String = ""
    @Column("chrx_cnt")               var chrXCnt:         Long = 0
    @Column("chry_cnt")               var chrYCnt:         Long = 0
    @Column("chrx_prop")              var chrXProp:        Double = 0.0
    @Column("chry_prop")              var chrYProp:        Double = 0.0
    @Column("pred_sex")               var predSex:         String = ""
    @Column("chrx_cnt_tmp")           var chrXCntTmp:      Long = 0
    @Column("chry_cnt_tmp")           var chrYCntTmp:      Long = 0
    @Column("chrx_prop_tmp")          var chrXPropTmp:     Double = 0.0
    @Column("chry_prop_tmp")          var chrYPropTmp:     Double = 0.0
    @Column("pred_sex_tmp")           var predSexTmp:      String = ""

    @Id @Transient           lateinit var _id:             AnalysisQCPK
    companion object {
        data class AnalysisQCPK(
            val sample:  Long,
            val service: String,
            val batch:   String,
            val row:     Int
        )
    }

    override fun getId(): AnalysisQCPK = AnalysisQCPK(sample, service, batch, row)
    override fun isNew(): Boolean = !this::createAt.isInitialized
}