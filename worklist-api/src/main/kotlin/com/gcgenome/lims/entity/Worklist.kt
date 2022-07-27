package com.gcgenome.lims.entity

import com.infobip.spring.data.jdbc.annotation.processor.Schema
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Schema("avoid")
@Table("worklist")
data class Worklist(
    @CreatedDate @Column("create_at")    var createAt:  LocalDateTime? = null,
    val domain:    String? = null,
    @Id                                  val id:        UUID,
    val idx:       Int? = null,
    @Column("last_modify_at") val lastModifyAt: LocalDateTime? = null,
    val prefix:    String? = null,
    val remark:    String? = null,
    val serial:    String? = null,
    val status:    Status? = null,
    val title:     String? = null

) : Serializable {
    companion object {
        enum class Status{
            NORMAL, PRE_CREATE, POST_CREATE, DISPOSAL, MERGED, SEQUENCING, ANALYZED, COMPLETE
        }
    }
}