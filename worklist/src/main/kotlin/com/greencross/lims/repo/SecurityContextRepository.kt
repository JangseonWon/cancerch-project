package com.greencross.lims.repo

import com.greencross.lims.entity.User
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(
    private val repo: UserRepository
) : ServerSecurityContextRepository {
    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        return Mono.empty()
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        val user = exchange.request.headers.getFirst("X-USER-ID")?:throw RuntimeException("Not Authorized")
        return repo.findById(user).map { u->SecurityContextImpl(UserAuthentication(u)) }
    }
    class UserAuthentication(val entity: User): Authentication {
        override fun getName(): String {
            return entity.name
        }

        override fun getAuthorities(): Collection<GrantedAuthority> {
            return emptyList()
        }

        override fun getCredentials(): Any {
            TODO("Not yet implemented")
        }

        override fun getDetails(): User {
            return entity
        }

        override fun getPrincipal(): String {
            return entity.email
        }

        override fun isAuthenticated(): Boolean {
            return true
        }

        override fun setAuthenticated(isAuthenticated: Boolean) {
            TODO("Not yet implemented")
        }
    }
}