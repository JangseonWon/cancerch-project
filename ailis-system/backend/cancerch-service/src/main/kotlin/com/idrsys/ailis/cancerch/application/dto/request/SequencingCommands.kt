package com.idrsys.ailis.cancerch.application.dto.request

/**
 * Sequencing 생성 커맨드
 */
data class CreateSequencingCommand(
    val worklistId: Long,
    val index: Int,
    val sampleId: String,
    val barcode: String,
    val indexName: String,
    val qc: String? = null
)

/**
 * Sequencing QC 업데이트 커맨드
 */
data class UpdateSequencingQcCommand(
    val id: Long,
    val qc: String
)

/**
 * Sequencing 상태 업데이트 커맨드
 */
data class UpdateSequencingStateCommand(
    val id: Long,
    val state: String
)

/**
 * Sequencing 조회 쿼리
 */
data class GetSequencingQuery(
    val id: Long
)

/**
 * Sequencing 목록 조회 쿼리
 */
data class ListSequencingsQuery(
    val worklistId: Long? = null,
    val state: String? = null,
    val page: Int = 0,
    val size: Int = 20
)
