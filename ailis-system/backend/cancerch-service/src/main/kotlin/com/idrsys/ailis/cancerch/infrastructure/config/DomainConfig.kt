package com.idrsys.ailis.cancerch.infrastructure.config

import com.idrsys.ailis.cancerch.domain.service.WorklistDomainService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Domain Layer 빈 설정
 *
 * Domain Service는 프레임워크 독립적이므로
 * Infrastructure 계층에서 빈으로 등록
 */
@Configuration
class DomainConfig {

    @Bean
    fun worklistDomainService(): WorklistDomainService {
        return WorklistDomainService()
    }
}
