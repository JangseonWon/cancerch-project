package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Schema("avoid")
@Table("analysis_file")
data class AnalysisFile(
    @Id                                  val id: UUID
) : Serializable {
    @CreatedDate
    @Column("create_at")        lateinit var createAt:      LocalDateTime
    @LastModifiedDate
    @Column("last_modify_at")   lateinit var lastModifyAt:  LocalDateTime
    @Column("name")             lateinit var name:          String
    @Column("path")             lateinit var path:          String
    @Column("process_at")       lateinit var processAt:     LocalDateTime
}