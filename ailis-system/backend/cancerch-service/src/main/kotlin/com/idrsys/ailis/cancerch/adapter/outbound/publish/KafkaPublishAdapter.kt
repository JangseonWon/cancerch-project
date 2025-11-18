package com.idrsys.ailis.cancerch.adapter.outbound.publish

import com.idrsys.ailis.cancerch.domain.publish.PublishEvent
import com.idrsys.ailis.cancerch.domain.publish.PublishRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class KafkaPublishAdapter : PublishRepository {

    private val logger = LoggerFactory.getLogger(KafkaPublishAdapter::class.java)
    private val publishHistory = ConcurrentHashMap<Long, MutableList<PublishEvent>>()

    override fun publish(event: PublishEvent) {
        // In real implementation, this would publish to Kafka
        logger.info("""
            ==========================================
            Publishing event to Kafka:
            Event ID: ${event.eventId}
            Report ID: ${event.reportId}
            Sample ID: ${event.sampleId}
            Service Code: ${event.serviceCode}
            File Path: ${event.filePath}
            Event Type: ${event.eventType}
            Timestamp: ${event.timestamp}
            Metadata: ${event.metadata}
            ==========================================
        """.trimIndent())

        // Store in history
        publishHistory.computeIfAbsent(event.reportId) { mutableListOf() }.add(event)

        // Simulate Kafka publish
        logger.info("Event published successfully to topic: report-events")
    }

    override fun getPublishHistory(reportId: Long): List<PublishEvent> {
        return publishHistory[reportId]?.toList() ?: emptyList()
    }
}
