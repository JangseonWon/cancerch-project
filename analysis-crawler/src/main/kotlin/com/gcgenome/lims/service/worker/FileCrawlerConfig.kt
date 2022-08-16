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
    fun qc() : FileCrawler<AnalysisQC> {
        return TSVFileCrawler(Path.of(tmp), "avoid_lims_qc.+", AnalysisQC::class.java)
    }

    @Bean
    fun result() : FileCrawler<AnalysisResult> {
        return TSVFileCrawler(Path.of(tmp), "avoid_lims_results.+", AnalysisResult::class.java)
    }
    @Bean
    fun processedDir() : File{
        return File(processed)
    }
}