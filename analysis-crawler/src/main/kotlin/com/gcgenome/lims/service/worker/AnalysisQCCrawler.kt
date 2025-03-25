package com.gcgenome.lims.service.worker

import com.gcgenome.file_reader.FileCrawler
import com.gcgenome.lims.data.AnalysisQC
import com.gcgenome.lims.service.analysisQC.AnalysisQCRepository
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
class AnalysisQCCrawler(
    val crawler : FileCrawler<AnalysisQC>,
    val dao: AnalysisQCRepository,
    val processed: File,
    val jandi: Webhook,
    val repo: RequestRepository
    ) {
    private val logger = LoggerFactory.getLogger(AnalysisQCCrawler::class.java)
    @Scheduled(fixedDelay=600000)
    fun updateStatus(){
        logger.info("QC Crawler : Started")
        crawler.getDTOs().forEach { (dtos, file) ->
            dtos.forEach {
                val primaries = it.primary.split("_")
                val batchRow = primaries[0].split("-")
                val sampleId = primaries[1].replace("-","")
                val batch = batchRow[0]+"-"+batchRow[1]
                val row = batchRow[2]
                try {
                    repo.findBySample(sampleId.toLong()).map { request->
                        logger.info("QC Crawl : ${request.sample} : ${request.service}")
                        val entity = com.gcgenome.lims.entity.AnalysisQC(sampleId.toLong(), request.service, batch, row.toInt()).apply {
                            this.freemix = it.freeMixFcA
                            this.rawReads = it.rawReadsMillFcA
                            this.dupRate = it.dupRateFcA
                            this.filterReads = it.filteredReadsMillFcA
                            this.mean = it.meanFcA
                            this.median = it.medianFcA
                            this.qc = it.qcFcA
                            this.gc = it.gcFcA
                            this.freemixTmp = it.freeMixFcB
                            this.rawReadsTmp = it.rawReadsMillFcB
                            this.dupRateTmp = it.dupRateFcB
                            this.filterReadsTmp = it.filteredReadsMillFcB
                            this.meanTmp = it.meanFcB
                            this.medianTmp = it.medianFcB
                            this.qcTmp = it.qcFcB
                            this.gcTmp = it.gcFcB
                            this.chrXCnt = it.chrXCntFcA
                            this.chrYCnt = it.chrYCntFcA
                            this.chrXProp = it.chrXPropFcA
                            this.chrYProp = it.chrYPropFcA
                            this.predSex = sexMapper(it.predSexFcA)
                            this.chrXCntTmp = it.chrXCntFcB
                            this.chrYCntTmp = it.chrYCntFcB
                            this.chrXPropTmp = it.chrXPropFcB
                            this.chrYPropTmp = it.chrYPropFcB
                            this.predSexTmp = sexMapper(it.predSexFcB)
                        }
                        logger.info("QC Crawl : $batch 배치 $row Sample")
                        dao.persist(entity).subscribeOn(Schedulers.boundedElastic()).subscribe()
                    }.subscribeOn(Schedulers.boundedElastic()).subscribe()
                } catch (except : NumberFormatException) {
                    logger.info("QC Crawl : $except : $batch 배치 $row Sample은 NTC거나 CONTROL입니다.")
                }
            }
            val folderName = file.name.split("_")[0]
            file.copyTo(File(processed.path + "/${folderName}/" + file.name), true)
            file.delete()
            logger.info("QC Crwal : Ended")
            jandi.sendWithConnectInfos("QC 결과 업로드가 완료되었습니다. o(￣▽￣)ｄ", listOf(ConnectInfo().title("업로드 대상 : ${file.name}")))
        }
    }
    private fun sexMapper(sexPred: String?) = when(sexPred){
        "male"     -> "M"
        "female"   -> "F"
        else       -> "?"
    }

}
