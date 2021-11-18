package com.greencross.lims.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("sample")
class Sample(
    @Id val id: Double,
    @Column("sample_type")
    val sampleType: String?=null,
    val remark: String?=null,
    val patient: String,
    val barcode: Double?=0.0

)