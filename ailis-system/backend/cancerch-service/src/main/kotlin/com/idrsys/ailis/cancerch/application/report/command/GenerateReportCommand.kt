package com.idrsys.ailis.cancerch.application.report.command

data class GenerateReportCommand(
    val sampleId: String,
    val serviceCode: String,
    val batch: String,
    val rowNumber: Int,
    val language: String = "ko"
)
