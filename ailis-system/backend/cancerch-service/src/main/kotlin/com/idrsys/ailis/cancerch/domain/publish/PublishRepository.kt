package com.idrsys.ailis.cancerch.domain.publish

interface PublishRepository {
    suspend fun publish(event: PublishEvent)
    suspend fun getPublishHistory(reportId: Long): List<PublishEvent>
}
