package com.idrsys.ailis.cancerch.domain.model

import java.time.Instant

/**
 * AnalysisQC 도메인 모델 (Entity)
 *
 * 분석 품질 관리(Quality Control) 정보
 */
data class AnalysisQC(
    val id: AnalysisId,
    val freemix: Double?,
    val rawReadsMillions: Double?,
    val dupRate: Double?,
    val gc: Double?,
    val totalReads: Long?,
    val chrxCnt: Long?,
    val chryCnt: Long?,
    val predSex: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
    val version: Int = 1
) {
    init {
        freemix?.let { require(it in 0.0..1.0) { "Freemix must be between 0 and 1" } }
        rawReadsMillions?.let { require(it >= 0.0) { "Raw reads (millions) must be non-negative" } }
        dupRate?.let { require(it in 0.0..1.0) { "Duplication rate must be between 0 and 1" } }
        gc?.let { require(it in 0.0..1.0) { "GC content must be between 0 and 1" } }
        totalReads?.let { require(it >= 0) { "Total reads must be non-negative" } }
        chrxCnt?.let { require(it >= 0) { "ChrX count must be non-negative" } }
        chryCnt?.let { require(it >= 0) { "ChrY count must be non-negative" } }
        predSex?.let { require(it in listOf("M", "F", "UNKNOWN")) { "Predicted sex must be M, F, or UNKNOWN" } }
    }

    /**
     * QC 정보 업데이트
     */
    fun update(
        freemix: Double? = this.freemix,
        rawReadsMillions: Double? = this.rawReadsMillions,
        dupRate: Double? = this.dupRate,
        gc: Double? = this.gc,
        totalReads: Long? = this.totalReads,
        chrxCnt: Long? = this.chrxCnt,
        chryCnt: Long? = this.chryCnt,
        predSex: String? = this.predSex
    ): AnalysisQC {
        return copy(
            freemix = freemix,
            rawReadsMillions = rawReadsMillions,
            dupRate = dupRate,
            gc = gc,
            totalReads = totalReads,
            chrxCnt = chrxCnt,
            chryCnt = chryCnt,
            predSex = predSex,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    /**
     * QC 통과 여부 확인
     */
    fun isPassed(): Boolean {
        // QC 통과 기준: freemix < 0.05, dupRate < 0.5
        val freemixPassed = freemix?.let { it < 0.05 } ?: true
        val dupRatePassed = dupRate?.let { it < 0.5 } ?: true
        return freemixPassed && dupRatePassed
    }

    companion object {
        /**
         * 새로운 AnalysisQC 생성
         */
        fun create(
            sampleId: String,
            serviceCode: String,
            batch: String,
            rowNumber: Int,
            freemix: Double? = null,
            rawReadsMillions: Double? = null,
            dupRate: Double? = null,
            gc: Double? = null,
            totalReads: Long? = null,
            chrxCnt: Long? = null,
            chryCnt: Long? = null,
            predSex: String? = null
        ): AnalysisQC {
            val now = Instant.now()
            val id = AnalysisId.from(sampleId, serviceCode, batch, rowNumber)

            return AnalysisQC(
                id = id,
                freemix = freemix,
                rawReadsMillions = rawReadsMillions,
                dupRate = dupRate,
                gc = gc,
                totalReads = totalReads,
                chrxCnt = chrxCnt,
                chryCnt = chryCnt,
                predSex = predSex,
                createdAt = now,
                updatedAt = now,
                version = 1
            )
        }
    }
}
