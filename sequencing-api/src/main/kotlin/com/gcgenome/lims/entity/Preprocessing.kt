package com.gcgenome.lims.entity


import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Schema("avoid")
@Table("preprocessing")
data class Preprocessing(
    @Column("worklist")             val worklist:               UUID,
    @Column("index")                val index:                  Int,
    @Column("state")                var state:                  String = "PENDING"
): Persistable<Preprocessing.Companion.PreprocessingPK> {
    @CreatedBy
    @Column("create_by")            lateinit var createBy:      String
    @CreatedDate
    @Column("create_at")            lateinit var createAt:      LocalDateTime
    @LastModifiedBy
    @Column("last_modify_by")       lateinit var lastModifyBy:  String
    @LastModifiedDate
    @Column("last_modify_at")       lateinit var lastModifyAt:  LocalDateTime
    @Id @Transient                  lateinit var _id:           PreprocessingPK

    constructor(worklist: UUID, index: Int, createBy: String, createAt: LocalDateTime, lastModifyBy: String, lastModifyAt: LocalDateTime, state: String): this(worklist, index){
        this.createBy = createBy
        this.createAt = createAt
        this.lastModifyBy = lastModifyBy
        this.lastModifyAt = lastModifyAt
        this.state = state
    }
    override fun getId(): PreprocessingPK = PreprocessingPK(worklist.toString(), index)
    override fun isNew(): Boolean = this::createAt.isInitialized.not()
    companion object {
        data class PreprocessingPK (
            val worklist: String,
            val index: Int
        )
    }
}