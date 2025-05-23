package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Schema("avoid")
@Table("worklist")
data class Worklist(
    @Id                                  val id:            UUID,
    @Column("title")                     val title:         String,
    @Column("create_at")                 val createAt:      LocalDateTime,
    @Column("create_by")                 val createBy:      String,
    @Column("status")                    val status:        String,
    @Column("remark")                    val remark:        String? = null,
    @Column("domain")                    val domain:        String,
    @Column("serial")                    var serial:        String? = null,
    @Column("prefix")                    var prefix:        String? = null,
    @Column("idx")                       var idx:           Int? = null,
    @Column("last_modify_at")            val lastModifyAt:  LocalDateTime? = null,
    @Column("serialize_by")              val serializeBy:   String? = null
) : Serializable {
}
