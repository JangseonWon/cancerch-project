package com.gcgenome.lims.service.worker

import com.gcgenome.file_reader.FileCrawler
import com.gcgenome.lims.data.AnalysisResult
import com.gcgenome.lims.service.analysisRS.AnalysisRSDao
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

@Configuration
class AnalysisRSCrawler(
    val crawler : FileCrawler<AnalysisResult>,
    val dao: AnalysisRSDao
    ) {
    @Scheduled(fixedDelay=1000*60*60)
    fun updateStatus(){
        crawler.getDTOs().forEach{(dtos, file) ->
            dtos.forEach{
                println(it)
                val primaries = it.primary.split("_")
                val batchRow = primaries[0].split("-")
                val sampleId = primaries[1].replace("-","")
                Tests.values().forEach { test ->
                    val entity = com.gcgenome.lims.entity.AnalysisResult(sampleId.toLong(), test.name).apply {
                        this.batch              = batchRow[0]
                        this.row                = batchRow[1].toInt()
                        this.cadEnsembleProb    = it.cadEnsembleProb
                        this.too5Pred           = it.too5Pred
                        this.too5FemsProb       = it.too5Fems
                        this.too6Pred           = it.too6Pred
                        this.too6FemsProb       = it.too6Fems
                        this.iscore             = it.iscore
                        this.result             = it.result
                    }
                    dao.merge(entity).block()
                }
            }
        }
    }

    enum class Tests{
        N201
    }
}