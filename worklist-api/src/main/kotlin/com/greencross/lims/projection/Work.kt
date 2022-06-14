package com.greencross.lims.projection

import com.greencross.lims.entity.User
import io.r2dbc.postgresql.codec.Json
import java.time.LocalDateTime

data class Work(
    val worklist:       String,
    val index:          Int,
    val samples:        String,
    val services:       String,
    val patientName:    String?,
    val mrns:           String?,
    val gid:            String,
    val json:           String?,
    val createBy:       User,
    val createAt:       LocalDateTime,
    val lastModifyBy:   User?,
    val lastModifyAt:   LocalDateTime?
    ) {
    companion object{
        data class WorkBuilder(
            val worklist:       String,
            val index:          Int,
            val samples:        String,
            val services:       String,
            val patientName:    String?,
            val mrns:           String?,
            val gid:            String,
            var json:           Json?,
            val createAt:       LocalDateTime,
            val createById:     String,
            val createBy:       String,
            val lastModifyAt:   LocalDateTime?,
            val lastModifyById: String?,
            val lastModifyBy:   String?
        ){
            fun build() = Work(worklist, index, samples, services, patientName, mrns, gid, json?.asString(), User(createById, createBy), createAt, User(lastModifyById, lastModifyBy), lastModifyAt)
        }
    }
}