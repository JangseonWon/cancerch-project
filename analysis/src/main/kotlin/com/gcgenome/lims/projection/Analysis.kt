package com.gcgenome.lims.projection

import com.gcgenome.lims.entity.User
import java.time.LocalDateTime

data class Analysis(
    //region Analysis
    val sample: Long,
    val service: String,
    val batch: String,
    val row: Int,
    val file: String,
    val value: String,
    val createAt: LocalDateTime,
    val createBy: User,
    val lastModifyAt: LocalDateTime,
    val lastModifyBy: User,
    //endregion

    //region work
    val serial: String,
    //endregion

    //region Request
    val dateRequest: LocalDateTime,
    val dateStart: LocalDateTime,
    val dateDue: LocalDateTime,
    val dateSampling: LocalDateTime,
    val registered: Boolean,
    val canceled: Boolean,
    val deleted: Boolean,
    //endregion

    //region Sample
    val barcode: Int,
    //endregion

    //region Patient
    val patientName: String,
    val mrn: String,
    val sex: String,
    val birth: LocalDateTime,
    val customer: String,
    //endregion

    //region Report
    val fileName: String,
    val fileSize: Int,
    val fileUrl: String,
    val reportCreatedAt: LocalDateTime,
    val reportCreatedBy: User,
    val publishAt: LocalDateTime,
    val publisher: User
    //endregion
) {
    companion object{
        data class AnalysisBuilder(
            val sample: Long,
            val service: String,
            val batch: String,
            val row: Int,
            val file: String,
            val value: String,
            val createAt: LocalDateTime,
            val createById: String,
            val createBy: String,
            val lastModifyAt: LocalDateTime,
            val lastModifyById: String,
            val lastModifyBy: String,
            val serial: String,
            val dateRequest: LocalDateTime,
            val dateStart: LocalDateTime,
            val dateDue: LocalDateTime,
            val dateSampling: LocalDateTime,
            val registered: Boolean,
            val canceled: Boolean,
            val deleted: Boolean,
            val barcode: Int,
            val patientName: String,
            val mrn: String,
            val sex: String,
            val birth: LocalDateTime,
            val customer: String,
            val fileName: String,
            val fileSize: Int,
            val fileUrl: String,
            val reportCreatedAt: LocalDateTime,
            val reportCreateById: String,
            val reportCreateByNm: String,
            val publishAt: LocalDateTime,
            val publisherId: String,
            val publisher: String
        ){
            fun build() = Analysis(
                sample,
                service,
                batch,
                row,
                file,
                value,
                createAt,
                User(createById, createBy),
                lastModifyAt,
                User(lastModifyById, lastModifyBy),
                serial,
                dateRequest,
                dateStart,
                dateDue,
                dateSampling,
                registered,
                canceled,
                deleted,
                barcode,
                patientName,
                mrn,
                sex,
                birth,
                customer,
                fileName,
                fileSize,
                fileUrl,
                reportCreatedAt,
                User(reportCreateById, reportCreateByNm),
                publishAt,
                User(publisherId, publisher)
            )
        }
    }
}