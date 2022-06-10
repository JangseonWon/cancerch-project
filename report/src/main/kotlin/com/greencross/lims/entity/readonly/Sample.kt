package com.greencross.lims.entity.readonly

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("public.sample")
data class Sample(
    @Id val id : Long
) {
    lateinit var patient: String
    lateinit var  sampleType: String
    lateinit var  sampleType2: String
    var barcode: Long? = 0
}