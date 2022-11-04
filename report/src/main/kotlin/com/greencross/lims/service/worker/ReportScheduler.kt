package com.greencross.lims.service.worker

import com.greencross.lims.service.report.ReportHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.scheduler.Schedulers

@Configuration
class ReportScheduler(private val reportHandler: ReportHandler) {
    @Scheduled(fixedDelay = 60000)
    fun print(){
        reportHandler.scheduleReports().subscribeOn(Schedulers.immediate()).subscribe()
    }
}