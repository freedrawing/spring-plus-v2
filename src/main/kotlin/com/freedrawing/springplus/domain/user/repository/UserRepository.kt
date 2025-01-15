package com.freedrawing.springplus.domain.user.repository

import com.freedrawing.springplus.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
}