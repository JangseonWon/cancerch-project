package com.idrsys.ailis.cancerch.application.dto.request

/**
 * Worklist 생성 Command
 */
data class CreateWorklistCommand(
    val name: String,
    val batchPrefix: String,
    val batchIndex: Int,
    val createdBy: String
)

/**
 * Worklist 업데이트 Command
 */
data class UpdateWorklistCommand(
    val id: Long,
    val name: String,
    val updatedBy: String
)

/**
 * Worklist 상태 변경 Command
 */
data class ChangeWorklistStatusCommand(
    val id: Long,
    val status: String,
    val updatedBy: String
)

/**
 * Sample 추가 Command
 */
data class AddSampleToWorklistCommand(
    val worklistId: Long,
    val sampleId: Long,
    val barcode: String,
    val serviceCode: String,
    val rowNumber: Int,
    val updatedBy: String
)

/**
 * Sample 제거 Command
 */
data class RemoveSampleFromWorklistCommand(
    val worklistId: Long,
    val sampleId: Long,
    val updatedBy: String
)
