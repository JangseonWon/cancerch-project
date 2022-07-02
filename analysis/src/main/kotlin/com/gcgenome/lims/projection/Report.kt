package com.gcgenome.lims.projection

import com.gcgenome.lims.entity.User
import java.time.LocalDateTime
import java.util.*

data class Report(
    val sample: Long,
    val service: String,
    val createAt: LocalDateTime,
    val file: UUID,
    val createBy: User,
    val lastModifyAt: LocalDateTime,
    val lastModifyBy: User,
    val name: String,
    val size: Int,
    val publishAt: LocalDateTime,
    val publishBy: User,
    val publishLog: String
    ) {
    companion object {
        data class ReportBuilder(
            val sample:         Long,
            val service:        String,
            val createAt:       LocalDateTime,
            val file:           UUID,
            val createById:     String,
            val createByNm:     String,
            val lastModifyAt:   LocalDateTime,
            val lastModifyById: String,
            val lastModifyByNm: String,
            val name:           String,
            val size:           Int,
            val publishAt:      LocalDateTime,
            val publishById:    String,
            val publishByNm:    String,
            val publishLog:     String
        ){
            fun build() = Report(sample, service, createAt, file, User(createById, createByNm), lastModifyAt, User(lastModifyById, lastModifyByNm), name, size, publishAt, User(publishById, publishByNm), publishLog)
        }
    }
}