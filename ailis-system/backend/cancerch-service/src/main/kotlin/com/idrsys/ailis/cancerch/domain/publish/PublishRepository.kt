package com.idrsys.ailis.cancerch.domain.publish

interface PublishRepository {
    fun publish(event: PublishEvent)
    fun getPublishHistory(reportId: Long): List<PublishEvent>
}
