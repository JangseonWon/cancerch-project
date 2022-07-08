package com.greencross.lims.entity

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
) {
    @Column("create_time")  var createTime = LocalDateTime.now()
                            var name: String? = null
                            var extension: String? = null
                            var size: Long? = null
                            var sample: Long? = null
                            var service: String? = null
    @Column("bytes")        var data: ByteBuffer? = null
}