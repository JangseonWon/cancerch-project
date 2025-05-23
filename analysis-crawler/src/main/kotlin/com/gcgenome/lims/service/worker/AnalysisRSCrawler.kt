package com.gcgenome.lims.service.worker

import com.gcgenome.file_reader.FileCrawler
import com.gcgenome.lims.data.AnalysisResult
import com.gcgenome.lims.service.analysisRS.AnalysisRSRepository
import com.gcgenome.lims.service.request.RequestRepository
import com.gcgenome.querydsl.persist
import com.greencross.lims.jandiwebhook.Webhook
import com.greencross.lims.jandiwebhook.dto.ConnectInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Configuration
class AnalysisRSCrawler(
    val crawler: FileCrawler<AnalysisResult>,
    val dao: AnalysisRSRepository,
    val processed: File,
    val jandi: Webhook,
    val repo: RequestRepository,
    @Value("\${gcgenome.iscore-dir}") private val processedIscore: File,
    @Value("\${gcgenome.fems-dir}") private val processedFems: File,
    @Value("\${gcgenome.tmp-dir}") private val tmp: File,
    @Value("\${gcgenome.regex-fems}") regexFems: String,
    @Value("\${gcgenome.regex-iscore}") regexIscore: String
) {
    private val logger = LoggerFactory.getLogger(AnalysisRSCrawler::class.java)
    private val femsPattern = Regex(regexFems)
    private val iscorePattern = Regex(regexIscore)

    @Scheduled(fixedDelay = 600000)
    fun updateStatus() {
        logger.info("RS Crwal : Started")
        crawler.getDTOs().forEach { (dtos, file) ->
            dtos.forEach {
                val primaries = it.primary.split("_")
                val batchRow = primaries[0].split("-")
                val sampleId = primaries[1].replace("-", "")
                val batch = batchRow[0] + "-" + batchRow[1]
                val row = batchRow[2]
                try {
                    //
                    repo.findBySample(sampleId.toLong()).map { request ->
                        logger.info("${request.sample} ${request.service}")
                        val entity = com.gcgenome.lims.entity.AnalysisResult(
                            sampleId.toLong(),
                            request.service,
                            batch,
                            row.toInt()
                        ).apply {
                            this.cadEnsembleProb = it.cadEnsembleProb
                            this.too5Pred = it.too5Pred
                            this.too5FemsProb = it.too5Fems
                            this.too6Pred = it.too6Pred
                            this.too6FemsProb = it.too6Fems
                            this.iscore = it.iscore
                            this.result = it.result
                            this.femsCovBc = it.femsCovBc
                            this.femsBc = it.femsBc
                            this.covBc = it.covBc
                            this.femsCovBernn = it.femsCovBernn
                        }
                        logger.info("RS Crawl : $batch 배치 $row Sample")
                        dao.persist(entity).subscribeOn(Schedulers.boundedElastic()).subscribe()
                    }.subscribeOn(Schedulers.boundedElastic()).subscribe()
                } catch (except: NumberFormatException) {
                    logger.info("RS Crawl : $except : $batch 배치 $row Sample은 NTC거나 CONTROL입니다.")
                }
            }
            val folderName = file.name.split("_")[0]
            file.copyTo(File(processed.path + "/${folderName}/" + file.name), true)
            file.delete()
            logger.info("RS Crwal : Ended")
            jandi.sendWithConnectInfos("Result 결과 업로드가 완료되었습니다.", listOf(ConnectInfo().title("업로드 배치 : ${file.name}")))
        }
        Flux.fromIterable(
            tmp.listFiles { file ->
                file.isFile && file.extension.equals("png", ignoreCase = true)
            }?.toList().orEmpty()
        )
            .doOnSubscribe { logger.info("ImageCrawler 시작: tmpDir=${tmp.path}") }
            .flatMap { file ->
                when {
                    femsPattern.matches(file.name) -> processFile(file, "fems", processedFems)
                    iscorePattern.matches(file.name) -> processFile(file, "iscore", processedIscore)
                    else -> {
                        logger.info("${file.name}은 대상 파일이 아닙니다.")
                        Mono.empty<Boolean>()
                    }
                }
            }
            .collectList()
            .doFinally {
                logger.info("ImageCrawler 완료: femsDir=${processedFems.path}, iscoreDir=${processedIscore.path}")
            }
            .subscribe()
    }

    private fun moveFileTo(file: File, targetDir: File) {
        file.copyTo(File(targetDir.toPath().resolve(file.name).toString()), true)
        file.delete()
    }

    private fun splitFileNameToBatchAndRowAndService(fileName: String): Triple<String, Int, Long> {
        val parts = fileName.substringBeforeLast(".").split("_")
        return Triple(
            parts[0],               // batch
            parts[1].toInt(),       // row
            parts[2].replace("-", "").toLong()  // sample
        )
    }

    private fun setPathData(
        entity: com.gcgenome.lims.entity.AnalysisResult,
        path: String,
        type: String
    ): com.gcgenome.lims.entity.AnalysisResult =
        entity.apply {
            if (type == "fems") femsPath = path else iscorePath = path
        }

    private fun sendResultMessageToJandi(entity: com.gcgenome.lims.entity.AnalysisResult, type: String): Mono<Boolean> {
        jandi.sendWithConnectInfos(
            "DNA CT 검사의 $type 이미지가 입력됐습니다.",
            listOf(ConnectInfo().title("업로드 대상: ${entity.sample} / ${entity.service}"))
        )
        return Mono.just(true)
    }

    private fun processFile(file: File, type: String, targetDir: File): Flux<Boolean> {
        val (batch, row, sample) = splitFileNameToBatchAndRowAndService(file.name)
        val targetPath = targetDir.toPath().resolve(file.name).toString()

        return dao.findBySampleAndBatchAndRow(sample, batch, row)
            .flatMap { entity ->
                Mono.just(setPathData(entity, targetPath, type))
            }
            .flatMap { dao.persist(it) }
            .flatMap { sendResultMessageToJandi(it, type.toUpperCase()) }
            .doOnSubscribe {
                moveFileTo(file, targetDir)
            }
            .doOnError { e ->
                logger.error("처리 실패: ${file.name}", e)
            }
    }
}
