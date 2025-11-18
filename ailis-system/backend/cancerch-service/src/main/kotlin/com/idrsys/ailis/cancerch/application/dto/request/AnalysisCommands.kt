package com.idrsys.ailis.cancerch.application.dto.request

import java.time.Instant

/**
 * 분석 결과 업데이트 Command
 */
data class UpdateAnalysisResultCommand(
    val sampleId: String,
    val serviceCode: String,
    val batch: String,
    val rowNumber: Int,
    val cadEnsembleProb: Double? = null,
    val too5Pred: String? = null,
    val too6Pred: String? = null,
    val iscore: Double? = null,
    val result: String? = null,
    val comment: String? = null,
    val femsCovBc: Double? = null,
    val cfdnaConcentration: Double? = null,
    val status: String? = null,
    val analyzedAt: Instant? = null,
    val analyzedBy: String? = null
)

/**
 * QC 정보 업데이트 Command
 */
data class UpdateAnalysisQCCommand(
    val sampleId: String,
    val serviceCode: String,
    val batch: String,
    val rowNumber: Int,
    val freemix: Double? = null,
    val rawReadsMillions: Double? = null,
    val dupRate: Double? = null,
    val gc: Double? = null,
    val totalReads: Long? = null,
    val chrxCnt: Long? = null,
    val chryCnt: Long? = null,
    val predSex: String? = null
)

/**
 * 분석 결과 생성 Command
 */
data class CreateAnalysisResultCommand(
    val sampleId: String,
    val serviceCode: String,
    val batch: String,
    val rowNumber: Int,
    val cadEnsembleProb: Double? = null,
    val too5Pred: String? = null,
    val too6Pred: String? = null,
    val iscore: Double? = null,
    val result: String? = null,
    val comment: String? = null,
    val femsCovBc: Double? = null,
    val cfdnaConcentration: Double? = null,
    val qc: UpdateAnalysisQCCommand? = null,
    val status: String? = null,
    val analyzedAt: Instant? = null,
    val analyzedBy: String? = null
)
