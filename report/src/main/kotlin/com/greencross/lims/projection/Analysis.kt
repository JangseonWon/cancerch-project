package com.greencross.lims.projection

import com.gcgenome.lims.entity.User
import com.greencross.lims.entity.readonly.Patient
import com.greencross.lims.entity.readonly.Sample
import io.r2dbc.postgresql.codec.Json
import java.time.LocalDateTime

data class Analysis (
    val service: String,
    val batch: String,
    val row: Long,
    val sample: Sample,
    val patient: Patient,
    val createAt: LocalDateTime,
    val createBy: User,
    val lastModifyAt: LocalDateTime,
    val lastModifyBy: User,
    val file: String,
    val value: String?
) {
    companion object {
        data class AnalysisBuilder(
            val sample: Long,
            val service: String,
            val batch: String,
            val row: Long,
            val patient: String,
            val createId: String,
            val createAt: LocalDateTime,
            val createBy: String,
            val lastModifyId: String,
            val lastModifyAt: LocalDateTime,
            val lastModifyBy: String,
            val file: String,
            val value: Json?
        ){
//            fun build() = Analysis(service, batch, row, Sample(sample), Patient(), createAt, User(createId, createBy), lastModifyAt, User(lastModifyId, lastModifyBy), file,  value?.asString())
        }
    }
}