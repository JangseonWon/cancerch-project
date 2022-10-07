package com.gcgenome.lims.entity


import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Schema("avoid")
@Table("sequencing")
data class Sequencing(
    @Column("worklist")             val worklist:               UUID,
    @Column("index")                val index:                  Int,
    @Column("create_by")            val createBy:               String,
    @Column("create_at")            val createAt:               LocalDateTime,
    @Column("gid")                  val gid:                    String,
    @Column("samples")              val samples:                String?,
    @Column("services")             val services:               String?,
    @Column("patient_names")        val patient_names:          String?,
    @Column("mrns")                 val mrns:                   String?,
    @Column("qc")                   val qc:                     Boolean,
    @Column("i7_index")             val indexI7:                String,
    @Column("i7_sequence")          val sequenceI7:             String,
    @Column("i5_index")             val indexI5:                String,
    @Column("i5_sequence")          val sequenceI5:             String,
    @Column("state")                var state:                  String
) {
    @Id @Transient                  lateinit var _id:           SequencingPK
    companion object {
        data class SequencingPK(
            val worklist: String,
            val index: Int
        )
    }
}