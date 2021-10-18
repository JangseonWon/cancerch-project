package com.greencross.lims.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable

@Table("public.user")
data class User(
    @Id
    var id: String,
    var name: String
) : Serializable