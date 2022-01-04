package com.greencross.lims.report.builder

import com.greencross.lims.entity.Patient

enum class Sex {
    M, F;

    open fun from(sex: Patient.Sex): Sex? {
        return if (sex === Patient.Sex.M) M
        else if (sex === Patient.Sex.F) F
        else null
    }
}