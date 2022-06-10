package com.greencross.lims.service.user

import com.greencross.lims.entity.User
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository

interface UserRepository: QuerydslR2dbcRepository<User, String>