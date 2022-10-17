package com.greencross.lims.service.worker

import com.greencross.lims.service.report.ReportHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.scheduler.Schedulers

@Configuration
class ReportScheduler(private val reportHandler: ReportHandler) {
    @Scheduled(fixedDelay = 10000)
    fun go(){
        reportHandler.searchReports().subscribeOn(Schedulers.immediate()).subscribe()
    }
}