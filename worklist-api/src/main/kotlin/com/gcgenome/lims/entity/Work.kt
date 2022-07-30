package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Schema("avoid")
@Table("work")
data class Work(
    @Column("worklist")                 val worklist:       UUID,
    @Column("index")                    val index:          Int,
    @Column("samples")                  val samples:        String? = "",
    @Column("services")                 val services:       String? = "",
    @Column("patient_names")            val patientName:    String? = "",
    @Column("mrns")                     val mrns:           String? = "",
    @Column("gid")                      val gid:            String,
    @Column("x")                        val x:              Short,
    @Column("y")                        val y:              Short
): Persistable<Work.Companion.WorkPK> {
    @CreatedBy
    @Column("create_user")              lateinit var createBy: String
    @CreatedDate
    @Column("create_at")                lateinit var createAt: LocalDateTime
    @Id @Transient                      lateinit var _id: WorkPK
    constructor(
        worklist: UUID, index: Int, samples: String, services: String, patientName: String, mrns: String, gid: String, x: Short, y:Short, createBy: String, createAt: LocalDateTime
    ): this(worklist, index, samples, services, patientName, mrns, gid, x, y){
        this.createBy = createBy
        this.createAt = createAt
    }
    companion object {
        data class WorkPK(
            val worklist: UUID,
            val index: Int
        )
    }

    override fun getId(): WorkPK {
        return WorkPK(worklist, index)
    }

    override fun isNew(): Boolean {
        return false
    }
}