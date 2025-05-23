package com.gcgenome.lims.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("request_view")
data class Request(
    @Column("sample")  val sample: Long,
    @Column("service") val service: String
): Persistable<Request.Companion.RequestPK> {
    @Id
    @Transient
    lateinit var _id: RequestPK
    companion object {
        data class RequestPK(
            val sample: Long,
            val service: String
        )
    }

    override fun getId() = RequestPK(sample, service)

    override fun isNew() = false
}
