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
        val comment = analysis.comment?:""
        return Report(sample = analysis.sample, service = analysis.service, createAt = createTime).apply {
            this.batch = batch
            this.row = row
            this.language = lang
            this.isPrinted = "PREPARE"
            this.description = description
            this.resultInfo = Json.of(
                """
                {
                  "result": "$result", 
                  "result_type": "$prediction", 
                  "comment": "$comment", 
                  "text_report":  "${createTextReport(result, prediction, comment)}"
                }    
                """.trimIndent()

            )
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
    private fun createTextReport(result: String, predictedCancer: String, comment: String): String {
        val sb = StringBuilder()
        val guideline = when(result) {
            "일반관리" -> "-"
            "관심관리" -> "아이캔서치 검사 모니터링 권장 기간 : 3개월 후"
            "집중관리" -> "정밀검사 : 주치의와 상담요함 / 아이캔서치 검사 모니터링 권장 기간 : 3개월 후"
            else -> "오류"
        }
        sb.append("◆ 아이캔서치 분석 리포트\n\n")
        sb.append("■ 종합결과 : $result\n")
        sb.append("■ 이상 패턴 검출 여부 : ${if(result == "일반관리") "미검출" else "검출"}\n")
        sb.append("■ 6종 암 중 인공지능 예측 암종 : ${if(result != "집중관리") "해당없음" else predictedCancer}\n")
        sb.append("■ 기타 소견 : ${if(comment == "") "-" else comment}\n")
        sb.append("■ 가이드라인 : $guideline\n")
        sb.append("■ 검사자 : 김다솜 / 확인자 : 조은해\n")

        return sb.toString().replace("\n", "\\n").replace("\"", "\\\"")
    }

}
