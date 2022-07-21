package com.gcgenome.lims.entity

import java.time.Instant
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "batch")
class Batch {
    @Id var worklist: UUID? = null
    @Column(length = 16) var serial: String? = null     // 22AVD0001: serial = prefix + idx
    @Column(name = "create_at")
    var createAt: Instant? = null
    @Column(name = "create_by", length = 16)
    var createBy: String? = null
    @Column(name = "last_modify_at")
    var lastModifiedAt: Instant? = null
    @Column(name = "last_modify_by", length = 16)
    var lastModifiedBy: String? = null
    @Column(length = 16) var prefix: String? = null     // 22AVD
    @Column var idx: Int? = null                        // 0001

}