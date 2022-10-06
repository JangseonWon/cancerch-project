package com.gcgenome.lims.entity

import java.io.Serializable
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "preprocessing")
class Preprocessing {
    @EmbeddedId
    val pk : PreprocessingPK = PreprocessingPK()
    @Column(name = "create_at")
    var createAt: Instant? = null
    @Column(name = "create_by", length = 16)
    var createBy: String? = null
    @Column(name = "last_modify_at")
    var lastModifiedAt: Instant? = null
    @Column(name = "last_modify_by", length = 16)
    var lastModifiedBy: String? = null
    @Column(name = "conc_na")
    var concNa: Double? = null
    @Column(name = "conc_input")
    var concInput: Double? = null
    @Column(name = "lib_prep")
    var libPrep: String? = null
    @Column(name = "lib_conc_tapestation")
    var libConcTape: Double? = null
    @Column(name = "lib_conc_qubit")
    var libConcQubit: Double? = null
    @Column(name = "fragment_size")
    var fragmentSize: Double? = null
    @Column(name = "amount_in_nm")
    var amount: Double? = null
    @Column(name = "dilution")
    var dilution: Double? = null
    @Column(name = "volume")
    var volume: Double? = null
    @Column(name = "lib_volume")
    var libVolume: Double? = null
    @Column(name = "buffer_volume")
    var bufferVolume: Double? = null
    @Column(name = "i7_index", length=8)
    var indexI7: String? = null
    @Column(name = "i7_sequence", length=10)
    var sequenceI7: String? = null
    @Column(name = "i5_index", length=8)
    var indexI5: String? = null
    @Column(name = "i5_sequence", length=10)
    var sequenceI5: String? = null
    @Column(name = "qc")
    var qc: Boolean = true
    @Column(columnDefinition = "jsonb")
    var value: String? = null
    @Column(name = "state", length=9)
    var state: String? = null
    @Embeddable
    data class PreprocessingPK (
        @Column(name = "worklist", columnDefinition = "uuid", nullable = false, updatable = false)
        private var worklist: UUID = UUID.randomUUID(),
        @Column(name="\"index\"", nullable = false, updatable = false)
        var index: Short = 0
    ): Serializable
}