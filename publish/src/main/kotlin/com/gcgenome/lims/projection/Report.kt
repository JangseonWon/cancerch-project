package com.gcgenome.lims.projection

import com.fasterxml.jackson.databind.ObjectMapper
import com.gcgenome.lims.entity.User
import com.gcgenome.lims.model.ResultInfo
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
    val size: Long,
    val publishAt: LocalDateTime?,
    val publishBy: User?,
    val publishLog: String?,
    val resultInfo: String?
    ) {
    companion object {
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
            val size:           Long,
            val publishId:      String?,
            val publishAt:      LocalDateTime?,
            val publishBy:      String?,
            val publishLog:     Json?,
            val resultInfo:     Json?
        ){
            fun build() : Report{
                val publishUser : User? = if(publishId == null || publishBy == null) null else User(publishId, publishBy)
                val log : String = if(publishLog == null) "" else publishLog.asString()
                val resultJson: String = if(resultInfo == null) "" else resultInfo.asString()
                return Report(sample, service, file, createAt, User(createId, createBy), lastModifyAt, User(lastModifyId, lastModifyBy), name, size, publishAt, publishUser, log, resultJson)
            }
        }
    }
}
