package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable

@Schema("public")
@Table("service")
data class Service(
    @Id @Column("id")            val id: String
) : Serializable {
    @Column("name")     lateinit var name: String
}