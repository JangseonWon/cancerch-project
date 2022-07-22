package com.gcgenome.lims.data

data class Preprocessing(
    val worklist: String,
    val index: Int,
    val concNa: Double?,
    val concInput: Double?,
    val libPrep: String?,
    val libConcTape: Double?,
    val libConcQubit: Double?,
    val fragmentSize: Double?,
    val amount: Double?,
    val dilution: Double?,
    val volume: Double?,
    val libVolume: Double?,
    val bufferVolume: Double?,
    val indexI7: String?,
    val sequenceI7: String?,
    val indexI5: String?,
    val sequenceI5: String?
)

