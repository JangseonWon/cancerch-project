package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.time.LocalDateTime

@Schema("public")
@Table("patient")
data class Patient(
    @Id val id: String
) : Serializable {
    @Column("name") lateinit var name: String
    @Column("code") lateinit var code: String
    @Column("sex")  lateinit var sex:  String
    @Column("birth")lateinit var birth: LocalDateTime
    @Column("customer_code") lateinit var customerCode: String
    @Column("customer_name") lateinit var customerName: String
    @Column("customer_code2")lateinit var customerCode2: String
    @Column("customer_name2")lateinit var customerName2: String
    @Column("mrn")           lateinit var mrn: String
}