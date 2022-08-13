package com.gcgenome.lims.data

import com.gcgenome.file_reader.FileColumn

data class AnalysisResult(
    @FileColumn("primary_key")
    var primary:            String = "",
    @FileColumn("CAD_ensemble_prob")
    var cadEnsembleProb:    Double = 0.0,
    @FileColumn("TOO5_pred_class")
    var too5Pred:           String = "",
    @FileColumn("TOO5_rmd_fems_ensemble_prob")
    var too5Fems:           Double = 0.0,
    @FileColumn("TOO6_pred_class")
    var too6Pred:           String = "",
    @FileColumn("TOO6_rmd_fems_ensemble_prob")
    var too6Fems:           Double = 0.0,
    @FileColumn("Iscore_v1.6.4")
    var iscore:             Double = 0.0,
    @FileColumn("results")
    var result:             String = ""
) {
}