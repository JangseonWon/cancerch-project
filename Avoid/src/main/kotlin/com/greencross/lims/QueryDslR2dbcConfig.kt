package com.greencross.lims

import com.querydsl.sql.PostgreSQLTemplates
import com.querydsl.sql.SQLTemplates
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class QueryDslR2dbcConfig {
    @Bean
    open fun sqlTemplates(): SQLTemplates {
        return PostgreSQLTemplates()
    }
}