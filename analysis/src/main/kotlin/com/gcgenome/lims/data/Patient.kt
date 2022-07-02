package com.gcgenome.lims.data

data class Patient(
    var name: String?,
    var mrn: String?,
    var sex: String,
    var birth: String,
    var customer: String
) {
}