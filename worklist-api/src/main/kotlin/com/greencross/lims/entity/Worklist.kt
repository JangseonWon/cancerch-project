package com.greencross.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Schema("worklist")
@Table("worklist")
data class Worklist(
    @Id @Column("id")                            val id:        UUID
) {
    @Column("title")                    lateinit var title:     String
    @CreatedDate @Column("create_at")   lateinit var createAt:  LocalDateTime
    @Column("status")                   lateinit var status:    Status
    @Column("remark")                   lateinit var remark:    String

//    constructor(worklist: UUID, title: String, createAt: LocalDateTime, status: String, remark: String): this(worklist){
//        this.title = title
//        this.createAt = createAt
//        this.status = status
//        this.remark = remark
//    }
    companion object {
        enum class Status{
            NORMAL, PRE_CREATE, POST_CREATE, DISPOSAL, MERGED, SEQUENCING, ANALYZED, COMPLETE
        }
    }
}