package com.idrsys.ailis.cancerch.domain.model

import java.io.Serializable

/**
 * Analysis 복합 식별자
 *
 * sampleId, serviceCode, batch, rowNumber의 조합으로 분석 결과를 고유하게 식별
 */
data class AnalysisId(
    val sampleId: String,
    val serviceCode: String,
    val batch: String,
    val rowNumber: Int
) : Serializable {
    init {
        require(sampleId.isNotBlank()) { "Sample ID cannot be blank" }
        require(serviceCode.isNotBlank()) { "Service code cannot be blank" }
        require(batch.isNotBlank()) { "Batch cannot be blank" }
        require(rowNumber > 0) { "Row number must be positive" }
    }

    override fun toString(): String = "$sampleId-$serviceCode-$batch-$rowNumber"

    companion object {
        fun from(sampleId: String, serviceCode: String, batch: String, rowNumber: Int): AnalysisId {
            return AnalysisId(sampleId, serviceCode, batch, rowNumber)
        }

        fun parse(str: String): AnalysisId {
            val parts = str.split("-")
            require(parts.size == 4) { "Invalid AnalysisId format: $str" }
            return AnalysisId(
                sampleId = parts[0],
                serviceCode = parts[1],
                batch = parts[2],
                rowNumber = parts[3].toInt()
            )
        }
    }
}
