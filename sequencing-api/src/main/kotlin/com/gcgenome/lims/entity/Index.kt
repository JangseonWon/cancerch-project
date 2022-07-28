package com.gcgenome.lims.entity


import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Schema("avoid")
@Table("index_sequence")
data class Index (
    @Column("id")                   val id:                     String,
    @Column("type")                 val type:                   String,
    @Column("sequence")             var sequence:               String,
    @Column("plate")                var plate:                  String,
    @Column("position")             var position:               String
) {
    @Id @Transient                  lateinit var _id:           IndexPK
    companion object {
        data class IndexPK(
            val id: String,
            val type: String
        )
    }
}