package com.greencross.lims.projection

import com.greencross.lims.entity.readonly.User
import io.r2dbc.postgresql.codec.Json
import java.time.LocalDateTime
import java.util.*

data class Report(
    val sample: Long,
    val service: String,
    val file: UUID?,
    val createAt: LocalDateTime,
    val createBy: User,
    val lastModifyAt: LocalDateTime,
    val lastModifyBy: User,
    val name: String?,
    val size: Long?,
    val publishAt: LocalDateTime?,
    val publishBy: User?,
    val publishLog: String?,
    val isPrinted: String?,
    val language: String?
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
            val name:           String?,
            val size:           Long?,
            val publishId:      String?,
            val publishAt:      LocalDateTime?,
            val publishBy:      String?,
            val publishLog:     Json?,
            val isPrinted:      String?,
            val language:       String?
        ){
            fun build() : Report{
                val publishUser : User? = if(publishId == null || publishBy == null) null else User(publishId, publishBy)
                val log : String? = if(publishLog == null) "" else publishLog.asString()
                return Report(sample, service, file, createAt, User(createId, createBy), lastModifyAt, User(lastModifyId, lastModifyBy), name, size, publishAt, publishUser, log, isPrinted, language)
            }
        }
    }
}