package com.gcgenome.lims.data

import com.gcgenome.file_reader.FileColumn

data class AnalysisQC(
    @FileColumn(name = "primary_key")
    var primary:                String = "",
    @FileColumn("FREEMIX_fcA")
    var freeMixFcA:             Double = 0.0,
    @FileColumn("FREEMIX_fcB")
    var freeMixFcB:             Double = 0.0,
    @FileColumn("dup_rate_fcA")
    var dupRateFcA:             Double = 0.0,
    @FileColumn("dup_rate_fcB")
    var dupRateFcB:             Double = 0.0,
    @FileColumn("GC_fcA")
    var gcFcA:                  Double = 0.0,
    @FileColumn("GC_fcB")
    var gcFcB:                  Double = 0.0,
    @FileColumn("raw_reads(millions)_fcA")
    var rawReadsMillFcA:        Double = 0.0,
    @FileColumn("raw_reads(millions)_fcB")
    var rawReadsMillFcB:        Double = 0.0,
    @FileColumn("filtered_reads(millions)_fcA")
    var filteredReadsMillFcA:   Double = 0.0,
    @FileColumn("filtered_reads(millions)_fcB")
    var filteredReadsMillFcB:   Double = 0.0,
    @FileColumn("mean_is_fcA")
    var meanFcA:                Double = 0.0,
    @FileColumn("mean_is_fcB")
    var meanFcB:                Double = 0.0,
    @FileColumn("median_is_fcA")
    var medianFcA:              Double = 0.0,
    @FileColumn("median_is_fcB")
    var medianFcB:              Double = 0.0,
    @FileColumn("QC_fcA")
    var qcFcA:                  String = "",
    @FileColumn("QC_fcB")
    var qcFcB:                  String = ""
)