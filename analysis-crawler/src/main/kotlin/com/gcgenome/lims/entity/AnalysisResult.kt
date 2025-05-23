package com.gcgenome.lims.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("analysis_result")
data class AnalysisResult(
    @Column("sample")  val sample:  Long,
    @Column("service") val service: String,
    @Column("batch")   val batch:   String,
    @Column("row")     val row:     Int
): Persistable<AnalysisResult.Companion.AnalysisResultPK> {
    @CreatedDate
    @Column("create_at")
    lateinit var createAt: LocalDateTime
    @Column("cad_ensemble_prob")
             var cadEnsembleProb: Double = 0.0
    @Column("too5_pred")
             var too5Pred: String = ""
    @Column("too5_rmd_fems_ensemble_prob")
             var too5FemsProb: Double = 0.0
    @Column("too6_pred")
             var too6Pred: String = ""
    @Column("too6_rmd_fems_ensemble_prob")
             var too6FemsProb: Double = 0.0
    @Column("iscore")
             var iscore: Double = 0.0
    @Column("result")
             var result: String = ""
    @Column("fems_cov_bc")
             var femsCovBc: Double = 0.0
    @Column("fems_bc")
             var femsBc: Double = 0.0
    @Column("cov_bc")
             var covBc: Double = 0.0
    @Column("fems_cov_bernn")
             var femsCovBernn: Double = 0.0
    @Column("fems_path")
             var femsPath: String? = null
    @Column("iscore_path")
             var iscorePath: String? = null


    @Id @Transient
    lateinit var _id: AnalysisResultPK

    override fun getId(): AnalysisResultPK = AnalysisResultPK(sample, service, batch, row)
    override fun isNew(): Boolean = !this::createAt.isInitialized

    companion object {
        data class AnalysisResultPK(
            val sample:  Long,
            val service: String,
            val batch:   String,
            val row:     Int
        )
    }
}
