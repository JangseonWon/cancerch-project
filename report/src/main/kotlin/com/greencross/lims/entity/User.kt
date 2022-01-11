package com.greencross.lims.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("public.user")
data class User(
    @Id
    var email: String,
    var name: String,
    var password: String,
    var organization: String,
    var authority: String?,
    var department: String?,
    var state: Boolean?,
    @CreatedDate
    @Column("create_at")
    var createAt: LocalDateTime?,
    @Column("last_login_at")
    var lastLoginAt: LocalDateTime?,
    var ip: String?
)