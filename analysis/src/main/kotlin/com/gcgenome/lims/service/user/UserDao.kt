package com.gcgenome.lims.service.user

import com.gcgenome.lims.entity.User
import com.gcgenome.lims.entity.QUser.user
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserDao(private val repo: UserRepository) {
    fun findById(id: String): Mono<User> = repo.query {
        it.select(user).from(user).where(user.id.eq(id))
    }.one()
}