package com.greencross.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*
import kotlin.jvm.Transient

@Schema("avoid")
@Table("preprocessing")
data class Preprocessing(
    @Column("worklist")                      val worklist: String,
    @Column("index")                         val index: Int,
): Persistable<Preprocessing.Companion.PreprocessingPK> {
    @CreatedBy
    @Column("create_by")            lateinit var createBy: String
    @CreatedDate
    @Column("create_at")            lateinit var createAt: LocalDateTime
    @LastModifiedBy
    @Column("last_modify_by")                var lastModifyBy: String? = ""
    @LastModifiedDate
    @Column("last_modify_at")                var lastModifyAt: LocalDateTime? = null
    @Column("value")                         var json: Json? = null
    @Id
    @Transient                      lateinit var _id: PreprocessingPK

    constructor(worklist: String, index: Int, createBy: String, createAt: LocalDateTime, lastModifyBy: String, lastModifyAt: LocalDateTime, json: Json): this(worklist, index){
        this.createBy = createBy
        this.createAt = createAt
        this.lastModifyBy = lastModifyBy
        this.lastModifyAt = lastModifyAt
        this.json = json
    }

    companion object {
        data class PreprocessingPK(
            val worklist: String,
            val index: Int
        )
    }

    override fun getId(): PreprocessingPK {
        return PreprocessingPK(worklist, index)
    }

    override fun isNew(): Boolean {
        return this::createAt.isInitialized.not()
    }
}