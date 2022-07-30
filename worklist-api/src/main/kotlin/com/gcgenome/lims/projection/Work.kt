package com.gcgenome.lims.projection

import com.gcgenome.lims.entity.User
import java.time.LocalDateTime

data class Work(
    val worklist:       String,
    val index:          Int,
    val samples:        String?,
    val services:       String?,
    val patientName:    String?,
    val mrns:           String?,
    val gid:            String,
    val address:        String,
    val json:           String? = "",
    val createBy:       User,
    val createAt:       LocalDateTime,
    val lastModifyBy:   User?,
    val lastModifyAt:   LocalDateTime?,
    var concNa:         Double?,
    var concInput:      Double?,
    var libPrep:        String?,
    var libConcTape:    Double?,
    var libConcQubit:   Double?,
    var fragmentSize:   Double?,
    var amount:         Double?,
    var dilution:       Double?,
    var volume:         Double?,
    var libVolume:      Double?,
    var bufferVolume:   Double?,
    var qc:             Boolean,
    var indexI7:        String?,
    var sequenceI7:     String?,
    var indexI5:        String?,
    var sequenceI5:     String?
) {
    companion object{
        data class WorkBuilder(
            val worklist:       String,
            val index:          Int,
            val samples:        String?,
            val services:       String?,
            val patientName:    String?,
            val mrns:           String?,
            val gid:            String,
            val x:              Short,
            val y:              Short,
            val json:           String?,
            val createAt:       LocalDateTime,
            val createById:     String,
            val createBy:       String,
            val lastModifyAt:   LocalDateTime?,
            val lastModifyById: String?,
            val lastModifyBy:   String?,
            var concNa:         Double?,
            var concInput:      Double?,
            var libPrep:        String?,
            var libConcTape:    Double?,
            var libConcQubit:   Double?,
            var fragmentSize:   Double?,
            var amount:         Double?,
            var dilution:       Double?,
            var volume:         Double?,
            var libVolume:      Double?,
            var bufferVolume:   Double?,
            var qc:             Boolean,
            var indexI7:        String?,
            var sequenceI7:     String?,
            var indexI5:        String?,
            var sequenceI5:     String?
        ){
            fun build(): Work = Work(
                worklist = worklist,
                index = index,
                samples = samples,
                services = services,
                patientName = patientName,
                mrns = mrns,
                gid = gid,
                address = toAddress(x, y),
                json = json,
                createBy = User(createById, createBy),
                createAt = createAt,
                lastModifyBy = User(lastModifyById, lastModifyBy),
                lastModifyAt = lastModifyAt,
                concNa = concNa,
                concInput = concInput,
                libPrep = libPrep,
                libConcTape = libConcTape,
                libConcQubit = libConcQubit,
                fragmentSize = fragmentSize,
                amount = amount,
                dilution = dilution,
                volume = volume,
                libVolume = libVolume,
                bufferVolume = bufferVolume,
                qc = qc,
                indexI7 = indexI7,
                sequenceI7 = sequenceI7,
                indexI5 = indexI5,
                sequenceI5 = sequenceI5
            )
        }
        fun toAddress(x: Short, y: Short): String {
            var y = y.toInt()
            var address = ""
            do {
                address = 'A'.plus(y%26) + address
                y /= 26
            } while(y-- > 0)
            return "$address${(x+1).toString().padStart(2, '0')}"
        }
    }
}