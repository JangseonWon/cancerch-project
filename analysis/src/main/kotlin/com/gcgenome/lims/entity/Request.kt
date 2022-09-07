package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Schema("public")
@Table("request")
data class Request(
    @Column("sample")       val sample: Long,
    @Column("service")      val service: String
) : Persistable<Request.Companion.RequestPK> {
    @Column("date_request") lateinit var dateRequest:   LocalDateTime
    @Column("date_start")   lateinit var dateStart:     LocalDateTime
    @Column("date_due")     lateinit var dateDue:       LocalDateTime
    @Column("date_sampling")lateinit var dateSampling:  LocalDateTime
    @Column("tat")                   var tat:           Int? = 0
    @Column("register")              var registered:    Boolean? = false
    @Column("cancel")                var canceled:      Boolean? = false
    @Column("delete")                var deleted:       Boolean? = false
    @Id @Transient          lateinit var _id:           RequestPK

    companion object{
        data class RequestPK(
            val sample: Long,
            val service: String
        )
    }

    override fun getId(): RequestPK {
        return RequestPK(sample, service)
    }

    override fun isNew(): Boolean {
        return false
    }
}