package com.greencross.lims.entity.readonly

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Schema("public")
@Table("sample")
data class Sample(
    @Id val id : Long,
    var patient: String,
    @Column("sample_type")  var sampleType: String,
    @Column("sample_type2") var sampleType2: String,
    var barcode: Long? = 0
) {

}