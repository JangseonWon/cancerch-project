package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Schema("public")
@Table("request")
data class Request(
    val sample: Long,
    val service: String,
    @Column("date_request")val dateRequest: LocalDate,
    @Column("date_sampling") val dateSampling: LocalDate,
    @Column("date_due") val dateDue: LocalDate,
    @Column("date_reception") val dateReception: LocalDateTime,
    @Column("info") val info: String

): Persistable<Request.Companion.RequestPK> {
    @Id @Transient lateinit var _Id: RequestPK

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
