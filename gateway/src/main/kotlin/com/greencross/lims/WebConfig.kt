package com.greencross.lims

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.TimeUnit


@Configuration
@EnableAsync
@EnableWebFlux
open class WebConfig(private val objectMapper: ObjectMapper) : WebFluxConfigurer {
    @Bean
    @LoadBalanced
    open fun clientBuilder(): WebClient.Builder {
        return WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                .codecs { configurer: ClientCodecConfigurer ->
                    configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
                    configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
                }.build())
    }
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("file:/data/lims/static/avoid-service/")
//            .addResourceLocations("classpath:/static/")
            .setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES))
            .resourceChain(false)
    }
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
        configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
    }
}