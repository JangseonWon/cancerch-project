package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Schema("public")
@Table("work")
data class Work(
    @Column("sheet")        val sheet:      UUID,
    @Column("worklist")     val worklist:   Int,
    @Column("sample")       val sample:     Long,
    @Column("service")      val service:    String,
    @Column("serial")       val serial:     String
) : Persistable<Work.Companion.WorkPK> {
    @Id @Transient lateinit var _id:        WorkPK
    companion object{
        data class WorkPK(
            val sheet:      UUID,
            val work:       Int,
            val sample:     Long,
            val service:    String
        )
    }

    override fun getId(): WorkPK {
        return WorkPK(sheet, worklist, sample, service)
    }

    override fun isNew(): Boolean {
        return false;
    }
}