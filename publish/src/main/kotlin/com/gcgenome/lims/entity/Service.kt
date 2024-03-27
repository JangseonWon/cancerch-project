package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Schema("public")
@Table("service")
data class Service (
    @Id @Column("id")   val serviceId: String,
    @Column("name")     val serviceNm: String
): Persistable<String> {
    override fun getId(): String = serviceId

    override fun isNew(): Boolean = false

}
