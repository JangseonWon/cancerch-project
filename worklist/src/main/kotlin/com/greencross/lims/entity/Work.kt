package com.greencross.lims.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("work")
data class Work(
    var no: String,
    @Id
    var id: String,
    var barcode: Double? = 0.0,
    var batch: String? = "",
    @Column("req_num")
    var reqNum: String? = "",
    @Column("test_type")
    var testType: String? = "",
    @Column("test_code")
    var testCode: String? = "",
    @Column("pat_name")
    var patName: String? = "",
    var sample: Long? = 0,
    @Column("cus_name")
    var cusName: String? = "",
    @Column("cus_code")
    var cusCode: String? = "",
    @Column("end_dt")
    var endDt: LocalDateTime? = LocalDateTime.now(),
    @Column("dna_prep")
    var dnaPrep: String? = "",
    @Column("dna_method")
    var dnaMethod: String? = "",
    @Column("ext_dt")
    var extDt: LocalDateTime? = LocalDateTime.now(),
    @Column("dna_conc")
    var dnaConc: Double? = 0.0,
    @Column("dna_vol")
    var dnaVol: Double? = 0.0,
    @Column("dw_vol")
    var dwVol: Double? = 0.0,
    @Column("tot_amt")
    var totAmt: Double? = 0.0,
    @Column("lib_prep")
    var libPrep: String? = "",
    @Column("lib_method")
    var libMethod: String? = "",
    var index: Int? = 0,
    @Column("lib_conc")
    var libConc: Double? = 0.0,
    @Column("frag_size")
    var fragSize: Double? = 0.0,
    var mol: Double? = 0.0,
    var bps: Int? = 0,
    @Column("lib_pmol")
    var libPmol: Double? = 0.0,
    var multiple: Double? = 0.0,
    @Column("f_pool_vol")
    var fPoolVol: Double? = 0.0,
    @Column("elut_vol")
    var elutVol: Double? = 0.0,
    var worklist: String? = ""
)
