package com.greencross.lims.entity.readonly

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("public.service")
class Service (
    @Id @Column("id")   val serviceId: String,
    @Column("name")     val serviceNm: String
) {
}