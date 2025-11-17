package com.idrsys.ailis.cancerch.infrastructure.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class R2dbcConfig : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        // Spring Boot의 자동 설정을 사용하므로 실제로는 필요 없지만
        // AbstractR2dbcConfiguration을 상속하면 반드시 구현해야 함
        throw UnsupportedOperationException(
            "ConnectionFactory는 Spring Boot 자동 설정을 사용합니다"
        )
    }

    @Bean
    fun transactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }
}
