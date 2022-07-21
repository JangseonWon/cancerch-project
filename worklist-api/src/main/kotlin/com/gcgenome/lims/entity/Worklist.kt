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
    @Id                                  val id:        UUID,
    val title:     String? = null,
    @CreatedDate @Column("create_at")    var createAt:  LocalDateTime? = null,
    val status:    Status? = null,
    val remark:    String? = null,
    val domain:    String? = null,
    val prefix:    String? = null,
    val idx:       Int? = null,
    val serial:    String? = null
) : Serializable {
    companion object {
        enum class Status{
            NORMAL, PRE_CREATE, POST_CREATE, DISPOSAL, MERGED, SEQUENCING, ANALYZED, COMPLETE
        }
    }
}