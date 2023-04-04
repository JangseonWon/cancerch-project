package com.gcgenome.lims.service.worker

import com.gcgenome.file_reader.FileCrawler
import com.gcgenome.lims.data.AnalysisResult
import com.gcgenome.lims.service.analysisRS.AnalysisRSRepository
import com.gcgenome.lims.service.request.RequestRepository
import com.gcgenome.querydsl.persist
import com.greencross.lims.jandiwebhook.Webhook
import com.greencross.lims.jandiwebhook.dto.ConnectInfo
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.scheduler.Schedulers
import java.io.File

@Configuration
class AnalysisRSCrawler(
    val crawler : FileCrawler<AnalysisResult>,
    val dao: AnalysisRSRepository,
    val processed: File,
    val jandi: Webhook,
    val repo: RequestRepository
    ) {
    private val logger = LoggerFactory.getLogger(AnalysisRSCrawler::class.java)
    @Scheduled(fixedDelay=600000)
    fun updateStatus(){
        logger.info("RS Crwal : Started")
        crawler.getDTOs().forEach{(dtos, file) ->
            dtos.forEach{
                val primaries = it.primary.split("_")
                val batchRow = primaries[0].split("-")
                val sampleId = primaries[1].replace("-","")
                val batch = batchRow[0]+"-"+batchRow[1]
                val row = batchRow[2]
                try {
                    //
                    repo.findBySample(sampleId.toLong()).map { request->
                        logger.info("${request.sample} ${request.service}")
                        val entity = com.gcgenome.lims.entity.AnalysisResult(sampleId.toLong(), request.service, batch, row.toInt()).apply {
                            this.cadEnsembleProb = it.cadEnsembleProb
                            this.too5Pred = it.too5Pred
                            this.too5FemsProb = it.too5Fems
                            this.too6Pred = it.too6Pred
                            this.too6FemsProb = it.too6Fems
                            this.iscore = it.iscore
                            this.result = it.result
                        }
                        logger.info("RS Crawl : $batch 배치 $row Sample")
                        dao.persist(entity).subscribeOn(Schedulers.boundedElastic()).subscribe()
                    }.subscribeOn(Schedulers.boundedElastic()).subscribe()
                } catch (except : NumberFormatException){
                    logger.info("RS Crawl : $except : $batch 배치 $row Sample은 NTC거나 CONTROL입니다.")
                }
            }
            val folderName = file.name.split("_")[0]
            file.copyTo(File(processed.path + "/${folderName}/" + file.name), true)
            file.delete()
            logger.info("RS Crwal : Ended")
            jandi.sendWithConnectInfos("Result 결과 업로드가 완료되었습니다. ヽ(✿ﾟ▽ﾟ)ノ", listOf(ConnectInfo().title("업로드 대상 : ${file.name}")))
        }
    }

    enum class Tests{
        N201
    }
}