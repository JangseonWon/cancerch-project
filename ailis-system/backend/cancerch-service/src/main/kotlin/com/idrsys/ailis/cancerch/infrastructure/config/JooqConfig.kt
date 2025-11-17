package com.idrsys.ailis.cancerch.infrastructure.config

import io.r2dbc.spi.ConnectionFactory
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.r2dbc.core.DatabaseClient

@Configuration
class JooqConfig {

    @Bean
    fun dslContext(): DSLContext {
        return DSL.using(SQLDialect.POSTGRES)
    }

    @Bean
    fun databaseClient(connectionFactory: ConnectionFactory): DatabaseClient {
        return DatabaseClient.create(connectionFactory)
    }
}
