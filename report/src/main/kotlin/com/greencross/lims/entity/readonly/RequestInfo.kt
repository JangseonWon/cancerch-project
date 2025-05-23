package com.greencross.lims.entity.readonly

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Schema("public")
@Table("request_info")
data class RequestInfo(
    @Column("sample")   val sample : Long,
    @Column("service")  val service : String,
    @Column("code")     val code: String,
    @Column("value")    val value: String,
    @Column("desc")     val desc: String
):Persistable<RequestInfo.Companion.RequestInfoPK> {
    @Id @Transient lateinit var _id: RequestInfoPK

    companion object {
        data class RequestInfoPK(
            val sample: Long,
            val service: String,
            val code: String
        )
    }

    override fun getId(): RequestInfoPK{
        return RequestInfoPK(sample, service, code)
    }

    override fun isNew(): Boolean {
        return false
    }
}
