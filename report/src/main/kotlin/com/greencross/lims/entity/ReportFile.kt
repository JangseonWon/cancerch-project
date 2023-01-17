package com.greencross.lims.entity

import com.gcgenome.report.versions.cassandra.HasPdf
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.util.*

@Table("file")
data class ReportFile(
    @PrimaryKey
    val id: UUID
) : HasPdf {
    @Column("create_time")  var createTime = LocalDateTime.now()
                            var name: String? = null
                            var extension: String? = null
                            var size: Long? = null
                            var sample: Long? = null
                            var service: String? = null
    @Column("bytes")        var data: ByteBuffer? = null
    override fun getPdf(): ByteArray {
        return data!!.array()
    }
}