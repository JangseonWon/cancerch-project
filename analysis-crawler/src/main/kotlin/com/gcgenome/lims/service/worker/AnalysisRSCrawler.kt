package com.gcgenome.lims.service.worker

import com.gcgenome.file_reader.FileCrawler
import com.gcgenome.lims.data.AnalysisResult
import com.gcgenome.lims.service.analysisRS.AnalysisRSRepository
import com.gcgenome.querydsl.persist
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import java.io.File

@Configuration
class AnalysisRSCrawler(
    val crawler : FileCrawler<AnalysisResult>,
    val dao: AnalysisRSRepository,
    val processed: File
    ) {
    private val logger = LoggerFactory.getLogger(AnalysisRSCrawler::class.java)
    @Scheduled(fixedDelay=1000*60*60)
    fun updateStatus(){
        logger.info("RS Crwal : Started")
        crawler.getDTOs().forEach{(dtos, file) ->
            dtos.forEach{
                val primaries = it.primary.split("_")
                val batchRow = primaries[0].split("-")
                val sampleId = primaries[1].replace("-","")
                try {
                    Tests.values().forEach { test ->
                        val entity = com.gcgenome.lims.entity.AnalysisResult(sampleId.toLong(), test.name).apply {
                            this.batch = batchRow[0]
                            this.row = batchRow[1].toInt()
                            this.cadEnsembleProb = it.cadEnsembleProb
                            this.too5Pred = it.too5Pred
                            this.too5FemsProb = it.too5Fems
                            this.too6Pred = it.too6Pred
                            this.too6FemsProb = it.too6Fems
                            this.iscore = it.iscore
                            this.result = it.result
                        }
                        logger.info("RS Crawl : ${batchRow[0]} 배치 ${batchRow[1]} Sample")
                        dao.persist(entity).block()
                    }
                } catch (except : NumberFormatException){
                    logger.info("RS Crawl : ${except} : ${batchRow[0]} 배치 ${batchRow[1]} Sample은 NTC거나 CONTROL입니다.")
                }
            }
            val folderName = file.name.split("_")[0]
            file.copyTo(File(processed.path + "/${folderName}/" + file.name), true)
            file.delete()
            logger.info("RS Crwal : Ended")
        }
    }

    enum class Tests{
        N201
    }
}