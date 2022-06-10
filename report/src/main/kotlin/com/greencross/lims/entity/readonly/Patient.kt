package com.greencross.lims.entity.readonly

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("Patient")
data class Patient(
    @Id
    @Column
    var id_SET: String,
    var name: String,
    @Column
    var code: String? = "-",
    @Column
    var sex: String? = null
){
    @Column
    val birth: LocalDate? = null
    @Column
    val customerCode: String? = null
    @Column
    val customerName: String? = null
    @Column
    val customerCode2: String? = null
    @Column
    val customerName2: String? = null
    @Column
    val mrn: String? = null
    @Column
    val customerDeptName: String? = null
    @Column
    val ward: String? = null
    @Column
    val physician: String? = null

    fun sex(): Sex? {
        return if (sex == null) null else Sex.valueOf(sex!!)
    }

    fun sex(sex: Sex?): Patient? {
        if (sex == null) this.sex = null else this.sex = sex.name
        return this
    }

    enum class Sex {
        M, F
    }
}