package com.gcgenome.lims.service.worker

import com.gcgenome.file_reader.FileCrawler
import com.gcgenome.lims.data.AnalysisQC
import com.gcgenome.lims.service.analysisQC.AnalysisQCDao
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

@Configuration
class AnalysisQCCrawler(
    val crawler : FileCrawler<AnalysisQC>,
    val dao: AnalysisQCDao
    ) {
    @Scheduled(fixedDelay=1000*60*60)
    fun updateStatus(){
        crawler.getDTOs().forEach { (dtos, file) ->
            dtos.forEach {
                val primaries = it.primary.split("_")
                val batchRow = primaries[0].split("-")
                val sampleId = primaries[1].replace("-", "")
                AnalysisRSCrawler.Tests.values().forEach { test ->
                    val entity = com.gcgenome.lims.entity.AnalysisQC(sampleId.toLong(), test.name).apply {
                        this.batch              = batchRow[0]
                        this.row                = batchRow[1].toInt()
                        this.freemix            = it.freeMixFcA
                        this.rawReads           = it.rawReadsMillFcA
                        this.dupRate            = it.dupRateFcA
                        this.filterReads        = it.filteredReadsMillFcA
                        this.mean               = it.meanFcA
                        this.median             = it.medianFcA
                        this.qc                 = it.qcFcA
                        this.gc                 = it.gcFcA
                        this.freemixTmp         = it.freeMixFcB
                        this.rawReadsTmp        = it.rawReadsMillFcB
                        this.dupRateTmp         = it.dupRateFcB
                        this.filterReadsTmp     = it.filteredReadsMillFcB
                        this.meanTmp            = it.meanFcB
                        this.medianTmp          = it.medianFcB
                        this.qcTmp              = it.qcFcB
                        this.gcTmp              = it.gcFcB
                    }
                    dao.merge(entity).block()
                }
            }
        }
    }
}
