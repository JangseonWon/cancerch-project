package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable

@Schema("public")
@Table("sample")
class Sample(
    @Id @Column("id")                    val id:            Long
) : Serializable {
    @Column("patient")          lateinit var patient:       String
    @Column("sample_type")      lateinit var sampleType:    String
    @Column("sample_type2")     lateinit var sampleType2:   String
    @Column("sample_type3")     lateinit var sampleType3:   String
    @Column("barcode")                   var barcode:       Int? = 0
    @Column("remark")           lateinit var remark:        String
}