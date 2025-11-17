package com.idrsys.ailis.cancerch.domain.exception

/**
 * 도메인 예외 기본 클래스
 */
sealed class DomainException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * 엔티티를 찾을 수 없음
 */
class EntityNotFoundException(
    entityType: String,
    id: Any
) : DomainException("$entityType not found: $id")

/**
 * 비즈니스 규칙 위반
 */
class BusinessRuleViolationException(
    message: String
) : DomainException(message)

/**
 * 동시성 충돌 (Optimistic Lock)
 */
class ConcurrentModificationException(
    message: String
) : DomainException(message)

/**
 * 중복 엔티티
 */
class DuplicateEntityException(
    entityType: String,
    identifier: String
) : DomainException("$entityType already exists: $identifier")
