package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Schema("avoid")
@Table("analysis_view")
data class Analysis(
    @Column("sample")                   val sample : Long,
    @Column("service_id")               val serviceId: String,
    @Column("service_nm")               val serviceNm: String,
    @Column("batch")                    val batch: String,
    @Column("row")                      val row: Int,

    @Column("freemix")                  val freemix: Double = 0.0,
    @Column("raw_reads_millions")       val rawReadsMillions: Long = 0,
    @Column("dup_rate")                 val dupRate: Double = 0.0,
    @Column("gc")                       val gc: Double = 0.0,
    @Column("total_reads")              val totalReads: Double = 0.0,
    @Column("mean")                     val mean: Double = 0.0,
    @Column("median")                   val median: Double = 0.0,
    @Column("qc")                       val qc: String = "F",

    @Column("freemix_tmp")              val freemixTmp: Double = 0.0,
    @Column("raw_reads_millions_tmp")   val rawReadsMillionsTmp: Long = 0,
    @Column("dup_rate_tmp")             val dupRateTmp: Double = 0.0,
    @Column("gc_tmp")                   val gcTmp: Double = 0.0,
    @Column("total_reads_tmp")          val totalReadsTmp: Double = 0.0,
    @Column("mean_tmp")                 val meanTmp: Double = 0.0,
    @Column("median_tmp")               val medianTmp: Double = 0.0,
    @Column("qc_tmp")                   val qcTmp: String = "F",

    @Column("cad_ensemble_prob")    val cadEnsembleProb: Double = 0.0,
    @Column("too5_pred")                val too5Pred: String = "",
    @Column("too5_rmd_fems_ensemble_prob") val too5FemsProb: Double = 0.0,
    @Column("too6_pred")                val too6Pred: String = "",
    @Column("too6_rmd_fems_ensemble_prob") val too6FemsProb: Double = 0.0,
    @Column("iscore")                   val iscore: Double = 0.0,
    @Column("result")                   val result: String,

    @Column("analysis_at")              val analysisAt: LocalDateTime,
    @LastModifiedDate
    @Column("last_modify_at")           val lastModifyAt: LocalDateTime,
    @Column("last_modify_by_id")        val lastModifyById: String,
    @Column("last_modify_by_nm")        val lastModifyByNm: String,
    @Column("date_request")             val dateRequest: LocalDateTime,
    @Column("date_start")               val dateStart: LocalDateTime,
    @Column("date_due")                 val dateDue: LocalDateTime,
    @Column("date_sampling")            val dateSampling: LocalDateTime,
    @Column("register")                 val register: Boolean,
    @Column("cancel")                   val cancel: Boolean,
    @Column("delete")                   val delete: Boolean,
    @Column("barcode")                  val barcode: Long,
    @Column("patient_name")             val patientName: String?,
    @Column("mrn")                      val mrn: String?,
    @Column("sex")                      val sex: String,
    @Column("birth")                    val birth: LocalDate,
    @Column("customer_name")            val customerName: String,
    @Column("report_name")              val reportName: String?,
    @Column("size")                     val size: Int?,
    @Column("file")                     val analysisFile: String?,
    @Column("file_url")                 val reportUrl: String?,
    @Column("reported_at")              val reportedAt: LocalDateTime?,
    @Column("reported_by_id")           val reportedById: String?,
    @Column("reported_by_nm")           val reportedByNm: String?,
    @Column("publish_at")               val publishAt: LocalDateTime?,
    @Column("publish_by_id")            val publishById: String?,
    @Column("publish_by_nm")            val publishByNm: String?
): Persistable<Analysis.Companion.AnalysisPK> {
    @Id
    @Transient                  lateinit var _id: AnalysisPK

    companion object {
        data class AnalysisPK(
            val sample: Long,
            val serviceId: String
        )
    }

    override fun getId(): AnalysisPK {
        return AnalysisPK(sample, serviceId)
    }

    override fun isNew(): Boolean {
        return false
    }


}