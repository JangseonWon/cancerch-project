package com.idrsys.ailis.cancerch.domain.model

import java.time.Instant

/**
 * AnalysisResult 도메인 모델 (Entity)
 *
 * 암 검사 분석 결과 엔티티
 */
data class AnalysisResult(
    val id: AnalysisId,
    val cadEnsembleProb: Double?,
    val too5Pred: String?,
    val too6Pred: String?,
    val iscore: Double?,
    val result: String?,
    val comment: String?,
    val femsCovBc: Double?,
    val cfdnaConcentration: Double?,
    val qc: AnalysisQC?,
    val status: String = "PENDING",
    val analyzedAt: Instant? = null,
    val analyzedBy: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
    val version: Int = 1
) {
    init {
        cadEnsembleProb?.let { require(it in 0.0..1.0) { "CAD ensemble probability must be between 0 and 1" } }
        iscore?.let { require(it >= 0.0) { "iScore must be non-negative" } }
        femsCovBc?.let { require(it >= 0.0) { "FEMS coverage must be non-negative" } }
        cfdnaConcentration?.let { require(it >= 0.0) { "cfDNA concentration must be non-negative" } }
    }

    /**
     * 분석 결과 업데이트
     */
    fun updateResult(
        cadEnsembleProb: Double? = this.cadEnsembleProb,
        too5Pred: String? = this.too5Pred,
        too6Pred: String? = this.too6Pred,
        iscore: Double? = this.iscore,
        result: String? = this.result,
        comment: String? = this.comment,
        femsCovBc: Double? = this.femsCovBc,
        cfdnaConcentration: Double? = this.cfdnaConcentration,
        status: String = this.status,
        analyzedAt: Instant? = this.analyzedAt,
        analyzedBy: String? = this.analyzedBy
    ): AnalysisResult {
        return copy(
            cadEnsembleProb = cadEnsembleProb,
            too5Pred = too5Pred,
            too6Pred = too6Pred,
            iscore = iscore,
            result = result,
            comment = comment,
            femsCovBc = femsCovBc,
            cfdnaConcentration = cfdnaConcentration,
            status = status,
            analyzedAt = analyzedAt,
            analyzedBy = analyzedBy,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    /**
     * QC 정보 업데이트
     */
    fun updateQC(qc: AnalysisQC): AnalysisResult {
        require(qc.id == this.id) { "QC ID must match AnalysisResult ID" }
        return copy(
            qc = qc,
            updatedAt = Instant.now(),
            version = version + 1
        )
    }

    /**
     * 분석이 완료되었는지 확인
     */
    fun isComplete(): Boolean {
        return result != null && result.isNotBlank()
    }

    companion object {
        /**
         * 새로운 AnalysisResult 생성
         */
        fun create(
            sampleId: String,
            serviceCode: String,
            batch: String,
            rowNumber: Int,
            cadEnsembleProb: Double? = null,
            too5Pred: String? = null,
            too6Pred: String? = null,
            iscore: Double? = null,
            result: String? = null,
            comment: String? = null,
            femsCovBc: Double? = null,
            cfdnaConcentration: Double? = null,
            qc: AnalysisQC? = null,
            status: String = "PENDING",
            analyzedAt: Instant? = null,
            analyzedBy: String? = null
        ): AnalysisResult {
            val now = Instant.now()
            val id = AnalysisId.from(sampleId, serviceCode, batch, rowNumber)

            return AnalysisResult(
                id = id,
                cadEnsembleProb = cadEnsembleProb,
                too5Pred = too5Pred,
                too6Pred = too6Pred,
                iscore = iscore,
                result = result,
                comment = comment,
                femsCovBc = femsCovBc,
                cfdnaConcentration = cfdnaConcentration,
                qc = qc,
                status = status,
                analyzedAt = analyzedAt,
                analyzedBy = analyzedBy,
                createdAt = now,
                updatedAt = now,
                version = 1
            )
        }
    }
}
