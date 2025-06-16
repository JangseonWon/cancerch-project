package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Schema("public")
@Table("patient")
data class Patient(
    @Id
    @Column("id") val id_SET: String,
    @Column("name") val name: String,
    @Column("sex") val sex: String? = null,
    @Column("birth") val birth: LocalDate? = null,
    @Column("customer_name") val customerName: String = "",
    @Column("mrn") val mrn: String?
): Persistable<String> {
    @Column("customer_code") val customerCode: String? = null
    @Column("code") val code: String = "-"
    @Column("customer_code2") val customerCode2: String? = null
    @Column("customer_name2") val customerName2: String? = null
    @Column("customer_dept_name") val customerDeptName: String? = null
    @Column("ward") val ward: String? = null
    @Column("physician") val physician: String? = null
    @Column("age") val age: Int? = null
    override fun getId(): String = id_SET
    override fun isNew(): Boolean = false
}
