package com.gcgenome.lims.data

data class Analysis(
    val sample:                 Long,
    val service:                String,
    val batch:                  String,
    val row:                    Int,
) {
    var request:                Request? = null
    var report:                 Report? = null
    var file:                   String? = ""
    var createdAt:              String? = ""
    var lastModifyAt:           String? = ""
    var lastModifyBy:           User? = null

    var freemix:                Double = 0.0
    var rawReadsMillions:       Long =  0
    var dupRate:                Double = 0.0
    var gc:                     Double = 0.0
    var totalReads:             Double = 0.0
    var mean:                   Double = 0.0
    var median:                 Double = 0.0
    var qc:                     String = "F"
    var chrXCnt:                Long = 0
    var chrYCnt:                Long = 0
    var chrXProp:               Double = 0.0
    var chrYProp:               Double = 0.0
    var predSex:                String = "?"

    var freemixTmp:             Double = 0.0
    var rawReadsMillionsTmp:    Long =  0
    var dupRateTmp:             Double = 0.0
    var gcTmp:                  Double = 0.0
    var totalReadsTmp:          Double = 0.0
    var meanTmp:                Double = 0.0
    var medianTmp:              Double = 0.0
    var qcTmp:                  String = "F"
    var chrXCntTmp:             Long = 0
    var chrYCntTmp:             Long = 0
    var chrXPropTmp:            Double = 0.0
    var chrYPropTmp:            Double = 0.0
    var predSexTmp:             String = "?"

    var too5Pred:               String = ""
    var too5FemsProb:           Double = 0.0
    var too6Pred:               String = ""
    var too6FemsProb:           Double = 0.0
    var iscore:                 Double = 0.0
    var cadEnsembleProb:        Double = 0.0
    var result:                 String = ""
    var comment:                String = ""
    var femsCovBc:              Double = 0.0
    var femsBc:                 Double = 0.0
    var covBc:                  Double = 0.0
    var femsCovBernn:           Double = 0.0
    var femsPath:               String = ""
    var iscorePath:             String = ""
    var language:               String = ""
    var clinicalCancer:         String = ""
    var cfDnaConcentration:     Double = 0.0
}
