package com.idrsys.ailis.cancerch.application.dto.request

/**
 * 분석 결과 검색 Query
 */
data class SearchAnalysisQuery(
    val sampleId: String? = null,
    val serviceCode: String? = null,
    val batch: String? = null,
    val result: String? = null,
    val page: Int = 0,
    val size: Int = 20
)

/**
 * 분석 결과 상세 조회 Query
 */
data class GetAnalysisResultQuery(
    val sampleId: String,
    val serviceCode: String,
    val batch: String,
    val rowNumber: Int
)
