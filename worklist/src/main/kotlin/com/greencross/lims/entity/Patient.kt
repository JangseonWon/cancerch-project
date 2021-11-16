package com.greencross.lims.entity

import com.greencross.lims.data.Sex
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("patient")
class Patient(
    @Id val id: String,
    val name: String?=null,
    val code: String?=null,
    val sex: Sex?=null,
    @Column("customer_name") val customerName: String?=null,
    val mrn: String?=null
)