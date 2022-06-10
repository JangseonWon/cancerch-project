package com.greencross.lims.projection

import com.gcgenome.lims.entity.User
import io.r2dbc.postgresql.codec.Json
import java.time.LocalDateTime
import java.util.*

data class Report(
    val sample: Long,
    val service: String,
    val file: UUID,
    val createAt: LocalDateTime,
    val createBy: User,
    val lastModifyAt: LocalDateTime,
    val lastModifyBy: User,
    val name: String,
    val size: String,
    val publishAt: LocalDateTime,
    val publishBy: User,
    val publishLog: String?
) {
    companion object{
        data class ReportBuilder(
            val sample:         Long,
            val service:        String,
            val file:           UUID,
            val createId:       String,
            val createAt:       LocalDateTime,
            val createBy:       String,
            val lastModifyId:   String,
            val lastModifyAt:   LocalDateTime,
            val lastModifyBy:   String,
            val name:           String,
            val size:           String,
            val publishId:      String,
            val publishAt:      LocalDateTime,
            val publishBy:      String,
            val publishLog:     Json
        ){
            fun build() = Report(sample, service, file, createAt, User(createId, createBy), lastModifyAt, User(lastModifyId, lastModifyBy), name, size, publishAt, User(publishId, publishBy), publishLog.asString())
        }
    }
}