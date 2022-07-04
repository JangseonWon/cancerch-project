package com.greencross.lims.entity.readonly

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Schema("public")
@Table("Patient")
data class Patient(
    @Id
    @Column val id_SET: String,
    @Column val name: String,
    @Column val sex: String = "-",
    @Column val birth: LocalDateTime?,
    @Column val customerName: String = "",
    @Column val mrn: String?
){
    @Column val customerCode: String? = null
    @Column val code: String = "-"
    @Column val customerCode2: String? = null
    @Column val customerName2: String? = null
    @Column val customerDeptName: String? = null
    @Column val ward: String? = null
    @Column val physician: String? = null

    enum class Sex{
        M, F
    }
}