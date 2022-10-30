package com.gcgenome.lims.service.worker

import com.gcgenome.file_reader.FileCrawler
import com.gcgenome.lims.data.AnalysisQC
import com.gcgenome.lims.service.analysisQC.AnalysisQCRepository
import com.gcgenome.querydsl.persist
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import java.io.File

@Configuration
class AnalysisQCCrawler(
    val crawler : FileCrawler<AnalysisQC>,
    val dao: AnalysisQCRepository,
    val processed: File
    ) {
    private val logger = LoggerFactory.getLogger(AnalysisQCCrawler::class.java)
    @Scheduled(fixedDelay=1000*60*60)
    fun updateStatus(){
        logger.info("QC Crawler : Started")
        crawler.getDTOs().forEach { (dtos, file) ->
            dtos.forEach {
                val primaries = it.primary.split("_")
                val batchRow = primaries[0].split("-")
                val sampleId = primaries[1].replace("-", "")
                try {
                    AnalysisRSCrawler.Tests.values().forEach { test ->
                        val entity = com.gcgenome.lims.entity.AnalysisQC(sampleId.toLong(), test.name, batchRow[0], batchRow[1].toInt()).apply {
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
                        logger.info("QC Crawl : ${batchRow[0]} 배치 ${batchRow[1]} Sample")
                        dao.persist(entity).block()
                    }
                } catch (except : NumberFormatException){
                    logger.info("QC Crawl : ${except} : ${batchRow[0]} 배치 ${batchRow[1]} Sample은 NTC거나 CONTROL입니다.")
                }
            }
            val folderName = file.name.split("_")[0]
            file.copyTo(File(processed.path + "/${folderName}/" + file.name), true)
            file.delete()
            logger.info("QC Crwal : Ended")
        }
    }
    private fun sexMapper(sexPred: String) = when(sexPred){
        "male"     -> "M"
        "female"   -> "F"
        else       -> "?"
    }

}
