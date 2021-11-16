package com.greencross.lims.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("request")
class Request(
    @Id val sample: Double,
    @Column("date_request") val dateRequest: LocalDateTime? = null,
    val info: String? = null,
    val service: String
)
