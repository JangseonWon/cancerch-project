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
    @Column("customer_name2") val customerName2: String?,
    @Column("mrn") val mrn: String?
){
    @Column("code") val code: String = "-"

    enum class Sex{
        M, F
    }
}