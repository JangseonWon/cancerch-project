package com.gcgenome.lims.service.worker

import com.gcgenome.lims.entity.AnalysisResult
import com.gcgenome.lims.service.analysisRS.AnalysisRSRepository
import com.gcgenome.querydsl.persist
import com.greencross.lims.jandiwebhook.Webhook
import com.greencross.lims.jandiwebhook.dto.ConnectInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Configuration
class ImageCrawler(
    private val dao: AnalysisRSRepository,
    private val jandi: Webhook,
    @Value("\${gcgenome.iscore-dir}")
    private val processedIscore: File,
    @Value("\${gcgenome.fems-dir}")
    private val processedFems: File,
    @Value("\${gcgenome.tmp-dir}")
    private val tmp: File,
    @Value("\${gcgenome.regex-fems}")
    private val regexFems: String,
    @Value("\${gcgenome.regex-iscore}")
    private val regexIscore: String
) {
    private val logger = LoggerFactory.getLogger(ImageCrawler::class.java)
    private val femsPattern = Regex(regexFems)
    private val iscorePattern = Regex(regexIscore)

    @Scheduled(fixedDelay = 600000)
    fun updateStatus() {
        logger.info("Image Crawl : Started - ${tmp.path}")
        val dirPath = tmp.path

        val pngFiles: List<File> = File(dirPath).listFiles { file ->
            file.isFile && file.extension.equals("png", ignoreCase = true)
        }?.toList().orEmpty()

        pngFiles.stream().forEach { file ->
            if (femsPattern.matches(file.name)) {
                val obj = splitFileNameToBatchAndRowAndService(file.name)

                dao.findBySampleAndBatchAndRow(obj.third, obj.first, obj.second)
                    .map { moveFile(it, file, "fems") }
                    .map { setPathData(it, processedFems.path, "fems") }
                    .flatMap(dao::persist)
                    .map { sendResultMessageToJandi(it, "FEMS")}
                    .subscribeOn(Schedulers.boundedElastic()).subscribe()

            } else if (iscorePattern.matches(file.name)) {
                val obj = splitFileNameToBatchAndRowAndService(file.name)

                dao.findBySampleAndBatchAndRow(obj.third, obj.first, obj.second)
                    .map { moveFile(it, file, "iscore") }
                    .map { setPathData(it, processedIscore.path, "i-score") }
                    .flatMap(dao::persist)
                    .map { sendResultMessageToJandi(it, "iscore") }
                    .subscribeOn(Schedulers.boundedElastic()).subscribe()
            }

        }
        logger.info("Image Crawl : Ended - ${processedIscore.path}")
    }

    private fun splitFileNameToBatchAndRowAndService(fileName: String): Triple<String, Int, Long> {
        val splitter = fileName.split("_")
        return Triple(
            splitter[0],
            splitter[1].toInt(),
            splitter[2].replace("-", "").toLong()
        )
    }

    private fun setPathData(entity: AnalysisResult, path: String, type: String) = if(type == "fems") entity.apply { this.femsPath = path } else entity.apply { this.iscorePath = path }
    private fun moveFile(entity: AnalysisResult, file: File, type: String): AnalysisResult {
        Files.move(file.toPath(), if(type == "fems") processedFems.toPath().resolve(file.name) else processedIscore.toPath().resolve(file.name), StandardCopyOption.REPLACE_EXISTING)
        return entity
    }
    private fun sendResultMessageToJandi(entity: AnalysisResult, type: String): Mono<Boolean>{
        jandi.sendWithConnectInfos("DNA CT 검사의 ${type} 이미지가 입력됐습니다.", listOf(ConnectInfo().title("업로드 대상 : ${entity.sample} / ${entity.service}")))
        return Mono.just(true)
    }
}
