package com.greencross.lims.entity.readonly

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Schema("public")
@Table("Patient")
data class Patient(
    @Id
    @Column("id") val id_SET: String,
    @Column("name") val name: String,
    @Column("sex") val sex: String = "-",
    @Column("birth") val birth: LocalDate,
    @Column("customer_name") val customerName: String = "",
    @Column("mrn") val mrn: String?
){
    @Column("customer_code") val customerCode: String? = null
    @Column("code") val code: String = "-"
    @Column("customer_code2") val customerCode2: String? = null
    @Column("customer_name2") val customerName2: String? = null
    @Column("customer_dept_name") val customerDeptName: String? = null
    @Column("ward") val ward: String? = null
    @Column("physician") val physician: String? = null

    enum class Sex{
        M, F
    }
}