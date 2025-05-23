package com.gcgenome.lims.entity;

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Schema("public")
@Table("user")
data class User(
    @Id val id: String? = "",
    val name: String? = "",
    val role: String = ""
) {
    private fun isAdmin(): Boolean = role == "A"
    fun isMaster(): Boolean = (role == "M") || isAdmin()
}
