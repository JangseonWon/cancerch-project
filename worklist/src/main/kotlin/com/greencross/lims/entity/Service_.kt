package com.greencross.lims.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("service")
class Service_(
    @Id val id: String,
    val name: String? = null
)