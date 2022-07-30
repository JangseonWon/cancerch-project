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
    @Column("worklist")             val worklist:               String,
    @Column("index")                val index:                  Int,
    @Column("conc_na")              var concNa:                 Double? = null,
    @Column("conc_input")           var concInput:              Double? = null,
    @Column("lib_prep")             var libPrep:                String? = null,
    @Column("lib_conc_tapestation") var libConcTape:            Double? = null,
    @Column("lib_conc_qubit")       var libConcQubit:           Double? = null,
    @Column("fragment_size")        var fragmentSize:           Double? = null,
    @Column("amount_in_nm")         var amount:                 Double? = null,
    @Column("dilution")             var dilution:               Double? = null,
    @Column("volume")               var volume:                 Double? = null,
    @Column("lib_volume")           var libVolume:              Double? = null,
    @Column("buffer_volume")        var bufferVolume:           Double? = null,
    @Column("i7_index")             var indexI7:                String? = null,
    @Column("i7_sequence")          var sequenceI7:             String? = null,
    @Column("i5_index")             var indexI5:                String? = null,
    @Column("i5_sequence")          var sequenceI5:             String? = null,
    @Column("value")                var json:                   String? = null
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

    constructor(worklist: String, index: Int, createBy: String, createAt: LocalDateTime, lastModifyBy: String, lastModifyAt: LocalDateTime, json: String,
                concNa: Double?, concInput: Double?, libPrep: String?, libConcTape: Double?, libConcQubit: Double?,
                fragmentSize: Double?, amount: Double?, dilution: Double?, volume: Double?, libVolume: Double?, bufferVolume: Double?,
                indexI7: String?, sequenceI7: String?, indexI5: String?, sequenceI5: String?): this(worklist, index){
        this.createBy = createBy
        this.createAt = createAt
        this.lastModifyBy = lastModifyBy
        this.lastModifyAt = lastModifyAt
        this.json = json
        this.concNa = concNa
        this.concInput= concInput
        this.libPrep = libPrep
        this.libConcTape = libConcTape
        this.libConcQubit = libConcQubit
        this.fragmentSize = fragmentSize
        this.amount = amount
        this.dilution = dilution
        this.volume = volume
        this.libVolume = libVolume
        this.bufferVolume = bufferVolume
        this.indexI7 = indexI7
        this.sequenceI7 = sequenceI7
        this.indexI5 = indexI5
        this.sequenceI5 = sequenceI5
    }
    override fun getId(): PreprocessingPK = PreprocessingPK(UUID.fromString(worklist), index)
    override fun isNew(): Boolean = this::createAt.isInitialized.not()
    companion object {
        data class PreprocessingPK (
            val worklist: UUID,
            val index: Int
        )
    }
}