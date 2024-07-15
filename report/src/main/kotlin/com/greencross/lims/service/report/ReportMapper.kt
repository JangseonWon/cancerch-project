package com.greencross.lims.service.report

import com.fasterxml.jackson.databind.ObjectMapper
import com.greencross.lims.entity.Report
import com.greencross.lims.projection.Analysis
import io.r2dbc.postgresql.codec.Json
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

@Component
class ReportMapper(private val om: ObjectMapper) {
    fun toDto(entity: Report) : com.greencross.lims.data.Report_{
        return com.greencross.lims.data.Report_(entity.sample, entity.service, entity.createAt.toString()).apply{
            this.createBy = entity.createBy
            this.name = entity.name
        }
    }
    fun toMessageDto(entity: Report): com.gcgenome.lims.data.Report{
        return com.gcgenome.lims.data.Report()
            .sample(entity.sample)
            .service(entity.service)
            .createAt(entity.createAt.toString())
            .creator(entity.createBy)
    }
    fun createReportEntity(analysis: Analysis, batch: String, row: Long, lang: String, description: String): Report {
        val createTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(LocalDateTime.now().toInstant(OffsetDateTime.now().offset).toEpochMilli()),
            ZoneId.systemDefault()
        )
        val result = when(analysis.result) {
            "GENERAL" -> "일반관리"
            "CONCERN" -> "관심관리"
            "RISK"    -> "집중관리"
            else      -> "오류발생"
        }
        val prediction = if(analysis.patient.sex == "F") replaceResult(analysis.too6Pred) else replaceResult(analysis.too5Pred)
        return Report(sample = analysis.sample, service = analysis.service, createAt = createTime).apply {
            this.batch = batch
            this.row = row
            this.language = lang
            this.isPrinted = "PREPARE"
            this.description = description
            this.resultInfo = Json.of("{\"result\": \"$result\", \"result_type\": \"$prediction\"}")
        }
    }

    private fun replaceResult(result: String) = when(result) {
            "LuC" -> "폐암"
            "Panc" -> "췌장담도암"
            "HCC" -> "간암"
            "colon" -> "대장암"
            "Others" -> "기타암종"
            "ESO" -> "식도암"
            "OV" -> "난소암"
            else -> "오류"
    }

}
