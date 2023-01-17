package com.greencross.lims.service.report

import com.gcgenome.report.versions.TableInfo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReportVersionConfig {
    @Bean
    fun tableInfo() = TableInfo("Report", "sample", "service", "publish_at", "description", "file")
}