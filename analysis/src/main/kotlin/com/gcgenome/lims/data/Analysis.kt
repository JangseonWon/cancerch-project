package com.gcgenome.lims.data

data class Analysis(
    val sample: Long,
    val service: String
) {
    var batch: String? = ""
    var row: Int? = 0
    var request: Request? = null
    var report: Report? = null
    var file: String? = ""
    var createdAt: String? = ""
    var createdBy: User? = null
    var lastModifyAt: String? = ""
    var lastModifyBy: User? = null
    var freemix: Double = 0.0
    var rawReadsMillions: Long =  0
    var dupRate: Double = 0.0
    var gc: Double = 0.0
    var rawReads: Double = 0.0
    var totalReads: Double = 0.0
    var mean: Double = 0.0
    var median: Double = 0.0
    var flowcell: String = "C"
    var qc: String = "F"
    var cadEnsembleProb: Double = 0.0
    var too5Pred: String = ""
    var too5FemsProb: Double = 0.0
    var too6Pred: String = ""
    var too6FemsProb: Double = 0.0
    var iscore: Double = 0.0
    var result: String = ""
}
