package com.gcgenome

import com.greencross.lims.jandiwebhook.JandiWebhook
import com.greencross.lims.jandiwebhook.Webhook
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JandiWebhookConfig {
    @Value("{webhook.url}")
    lateinit var url: String

    @Bean
    fun webhook(): Webhook {
        return JandiWebhook(url)
    }
}