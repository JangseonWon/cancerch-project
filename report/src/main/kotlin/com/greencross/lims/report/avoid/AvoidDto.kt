package com.greencross.lims.report.avoid

import com.greencross.lims.report.HasServiceCode
import com.greencross.lims.report.builder.AbstractReportDto

class AvoidDto: AbstractReportDto(), HasServiceCode {
    var code = String
    var interpretation = String
    var variants: Map<AvoidTemplate.Gene, List<Variant>> = HashMap()

    class Variant{
        val gene = String
        val dnaChange = String
        val predictedAa = String
        val zygosity = String
        val clazz = String
    }
    override fun code(): String {
        return code.toString()
    }

}