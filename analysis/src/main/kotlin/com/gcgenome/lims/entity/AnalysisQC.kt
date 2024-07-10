package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

//============================ Analysis QC 데이터 설명 =====================================
// 현재(2024.07.01) AI팀에서 분석한 데이터는 분석을 2회 이상 수행함.
// 따라서 발생하는 데이터도 2개 이상으로, 실험자들이 해당 데이터를 조회할 필요갸 있다고 하여
// 1차 데이터와 2차데이터만 업로드하고 있음.
// 현재는 더 많은양의 데이터가 LIMS로 전송중이나, 조회요청이나 별다른 요청이 없어 별도 개발없이
// 현상유지하고 있음.
// 추후 AI의 데이터 Fix 시 해당 Entity와 Crawler에서 사용하는 Entity의 수정이 필요함.
//=========================================================================================

@Schema("avoid")
@Table("analysis_qc")
data class AnalysisQC(
    @Column("sample")                val sample:                Long,
    @Column("service")               val service:               String,
    @Column("batch")                 val batch:                 String,
    @Column("row")                   val row:                   Int
): Persistable<AnalysisQC.Companion.AnalysisQCPK> {
    @CreatedDate
    @Column("create_at")    lateinit var createAt:              LocalDateTime
    @Column("file")                  var file:                  String = ""
    @Column("freemix")               var freemix:               Double = 0.0
    @Column("raw_reads_millions")    var rawReadsMillions:      Long   = 0L
    @Column("dup_rate")              var dupRate:               Double = 0.0
    @Column("total_reads")           var totalReads:            Double = 0.0
    @Column("mean")                  var mean:                  Double = 0.0
    @Column("median")                var median:                Double = 0.0
    @Column("qc")                    var qc:                    String = ""
    @Column("gc")                    var gc:                    Double = 0.0
    @Column("chrx_cnt")              var chrxCnt:               Long   = 0L
    @Column("chry_cnt")              var chryCnt:               Long   = 0L
    @Column("chrx_prop")             var chrxProp:              Double = 0.0
    @Column("chry_prop")             var chryProp:              Double = 0.0
    @Column("pred_sex")              var predSex:               String = ""

    //추후 삭제해야하는 데이터 목록
    @Column("freemix_tmp")           var freemixTmp:            Double = 0.0
    @Column("raw_reads_millions_tmp")var rawReadMillionsTmp:    Long   = 0L
    @Column("dup_rate_tmp")          var dupRateTmp:            Double = 0.0
    @Column("total_reads_tmp")       var totalReadsTmp:         Double = 0.0
    @Column("mean_tmp")              var meanTmp:               Double = 0.0
    @Column("median_tmp")            var medianTmp:             Double = 0.0
    @Column("qc_tmp")                var qcTmp:                 String = ""
    @Column("gc_tmp")                var gcTmp:                 Double = 0.0
    @Column("chrx_cnt_tmp")          var chrxCntTmp:            Long   = 0L
    @Column("chry_cnt_tmp")          var chryCntTmp:            Long   = 0L
    @Column("chrx_prop_tmp")         var chrxPropTmp:           Double = 0.0
    @Column("chry_prop_tmp")         var chryPropTmp:           Double = 0.0
    @Column("pred_sex_tmp")          var predSexTmp:            String = ""
    //여기까지
    @Transient @Id          lateinit var _id:                   AnalysisQCPK
    companion object {
        data class AnalysisQCPK(
            val sample: Long,
            val service: String,
            val batch: String,
            val row: Int
        )
    }

    override fun getId(): AnalysisQCPK = AnalysisQCPK(sample, service, batch, row)

    override fun isNew(): Boolean = !::createAt.isInitialized
}
