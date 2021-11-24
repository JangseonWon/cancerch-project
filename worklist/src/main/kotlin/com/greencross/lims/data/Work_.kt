package com.greencross.lims.data

import java.time.LocalDateTime

data class Work_ (
    var no: Double,
    var id: String,
    var barcode: Double,
    var batch: String,
    var reqNum: String,
    var testType: String,
    var testCode: String,
    var patName: String,
    var sample: Double,
    var cusName: String,
    var cusCode: String,
    var endDt: LocalDateTime,
    var dnaPrep: String,
    var dnaMethod: String,
    var extDt: LocalDateTime,
    var dnaConc: Double,
    var dnaVol: Double,
    var dwVol: Double,
    var totAmt: Double,
    var libPrep: String,
    var libMethod: String,
    var index: Int,
    var libConc: Double,
    var fragSize: Double,
    var mol: Double,
    var bps: Int,
    var libPmol: Double,
    var multiple: Double,
    var fPoolVol: Double,
    var elutVol: Double
    )