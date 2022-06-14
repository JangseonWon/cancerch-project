package com.greencross.lims.service.user

import com.greencross.lims.entity.User
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: QuerydslR2dbcRepository<User, String>