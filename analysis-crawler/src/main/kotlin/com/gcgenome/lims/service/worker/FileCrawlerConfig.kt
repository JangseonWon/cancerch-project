package com.gcgenome.lims.service.worker

import com.gcgenome.file_reader.FileCrawler
import com.gcgenome.file_reader.TSVFileCrawler
import com.gcgenome.lims.data.AnalysisQC
import com.gcgenome.lims.data.AnalysisResult
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.nio.file.Path

@Configuration
class FileCrawlerConfig {
    @Value("\${gcgenome.tmp-dir}")
    lateinit var tmp: String
    @Value("\${gcgenome.out-dir}")
    lateinit var processed: String

    @Bean
    fun qc_AVD() : FileCrawler<AnalysisQC> {
        return TSVFileCrawler(Path.of(tmp), "[0-9]{2}AVD[0-9]{3}-[0-9]{4}_qc\\.txt", AnalysisQC::class.java)
    }

    @Bean
    fun result_AVD() : FileCrawler<AnalysisResult> {
        return TSVFileCrawler(Path.of(tmp), "[0-9]{2}AVD[0-9]{3}-[0-9]{4}_results\\.txt", AnalysisResult::class.java)
    }
    @Bean
    fun qc_AIC() : FileCrawler<AnalysisQC> {
        return TSVFileCrawler(Path.of(tmp), "[0-9]{2}AIC[0-9]{3}-[0-9]{4}_qc\\.txt", AnalysisQC::class.java)
    }

    @Bean
    fun result_AIC() : FileCrawler<AnalysisResult> {
        return TSVFileCrawler(Path.of(tmp), "[0-9]{2}AIC[0-9]{3}-[0-9]{4}_results\\.txt", AnalysisResult::class.java)
    }
    @Bean
    fun processedDir() : File{
        return File(processed)
    }
}