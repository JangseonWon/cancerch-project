package com.greencross.lims.report.avoid

import com.greencross.lims.report.builder.AbstractReportTemplate
import com.greencross.lims.test.avoid.TestInfo

interface AvoidTemplate<R: AvoidResource>: AbstractReportTemplate<R> {
    interface Gene{
        fun name(): String
        fun diseaseRelated(): String
        fun cancerRelated(): String
        fun transcript(): String
        fun tier(): Tier
    }
    enum class Tier{
        Tier1, Tier2, Tier3
    }
    fun testInfo(): TestInfo
    fun genes(): Array<Gene>
    fun lblSummary(): String
    fun lblGene(): String
    fun lblDiseaseRelated(): String
    fun lblCancerRelated(): String
    fun lblResult(): String

    fun lblVariants(): String
    fun lblDnaVariant(): String
    fun lblAAVariant(): String
    fun lblZygosity(): String
    fun lblClass(): String

    fun lblInterpretation(): String

    fun lblTestInfo(): String
    fun lblTargetGenes(): String
    fun lblTestMethod(): String
    fun testMethod(): String
    fun lblReferenceSequences(): String { return "Reference Sequence" }
    fun lblReference(): String { return "References" }
    fun references(): Array<String>

    fun lblClinicalMeanings(): String
    fun lblClinicalMeanings(tier: Tier): String
    fun lblClinicalMeaningInfo(): String
    fun clinicalReference(gene: Gene): String
    fun clinicalMeaning(gene: Gene): String

}