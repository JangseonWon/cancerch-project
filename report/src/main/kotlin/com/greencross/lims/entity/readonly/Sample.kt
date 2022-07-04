package com.greencross.lims.entity.readonly

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Schema("public")
@Table("sample")
data class Sample(
    @Id val id : Long,
    var patient: String,
    var sampleType: String,
    var sampleType2: String,
    var barcode: Long? = 0
) {

}