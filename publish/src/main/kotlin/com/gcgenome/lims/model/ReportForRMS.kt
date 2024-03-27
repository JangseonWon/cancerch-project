package com.gcgenome.lims.model

data class ReportForRMS(
    val institutionName: String?,
    val departmentName: String?,
    val wardName: String?,
    val serviceName: String,
    val patientName: String,
    val sex: String,
    val birth: String,
    val mrn: String?,
    val info: String?,
    val institution: String?,
    val physician: String?,
    val sample: Long,
    val service: String,
    val report: String
) {
    val interpretation: Map<String, Any> = mutableMapOf()
    val customeInfos: Map<String, Any> = mutableMapOf()
}
