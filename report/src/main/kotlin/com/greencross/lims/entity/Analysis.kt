package com.greencross.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Schema("avoid")
@Table("analysis_result")
data class Analysis(
    @Column("sample")                    val sample:                Long,
    @Column("service")                   val service:               String
): Persistable<Analysis.Companion.AnalysisPK> {
    @Column("batch")            lateinit var batch:                 String
    @Column("row")                       var row:                   Long?   = null
    @Column("file")             lateinit var file:                  String
    @Column("result")           lateinit var result:                String
    @Column("too5_pred")        lateinit var too5Pred:              String
    @Column("too6_pred")        lateinit var too6Pred:              String
    @Column("comment")                   var comment:               String  = ""
    @Column("iscore")                    var iscore:                Double? = 0.0
    @Column("fems_cov_bc")               var femsCovBc:             Double? = 0.0
    @Column("fems_bc")                   var femsBc:                Double? = 0.0
    @Column("cov_bc")                    var covBc:                 Double? = 0.0
    @Column("fems_cov_bernn")            var femsCovBernn:          Double? = 0.0
    @Column("fems_path")                 var femsPath:              String? = ""
    @Column("iscore_path")               var iscorePath:            String? = ""
    @Column("cfdna_concentration")       var cfDNAContentration:    Double? = 0.0
    @Id @Transient lateinit var _id: AnalysisPK

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
