package com.greencross.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*
import kotlin.jvm.Transient

@Schema("worklist")
@Table("work")
data class Work(
    @Column("worklist")                  val worklist:      String,
    @Column("index")                     val index:         Int
): Persistable<Work.Companion.WorkPK> {
    @Column("samples")                   lateinit var samples:       String
    @Column("services")                  lateinit var services:      String
    @Column("patient_names")             lateinit var patientName:   String
    @Column("mrns")                      lateinit var mrns:          String
    @Column("gid")                       lateinit var gid:           String
    @CreatedBy
    @Column("create_user")               lateinit var createBy: String
    @CreatedDate
    @Column("create_at")                 lateinit var createAt: LocalDateTime
    @Id @Transient                       lateinit var _id:           WorkPK
    constructor(worklist: String, index: Int, samples: String, services: String, patientName: String, mrns: String, gid: String, createBy: String, createAt: LocalDateTime): this(worklist, index){
        this.samples = samples
        this.services = services
        this.patientName = patientName
        this.mrns = mrns
        this.gid = gid
        this.createBy = createBy
        this.createAt = createAt
    }
    companion object {
        data class WorkPK(
            val worklist: String,
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