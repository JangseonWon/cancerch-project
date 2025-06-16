package com.gcgenome

import com.gcgenome.lims.workflow.*
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

@Configuration
@EnableAspectJAutoProxy
class EventConfig {
    private val logger = LoggerFactory.getLogger(EventConfig::class.java)

    @Publish
    fun publishEvent(event: Event): List<Event> {
        try {
            return listOf(event)
        } catch (e: Exception) {
            logger.error("WORKFLOW COMPLETE EVENT 발송에 실패 했습니다.")
            e.printStackTrace()
            return listOf()
        }
    }
}
