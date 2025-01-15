package com.freedrawing.springplus.domain.auth

import com.freedrawing.springplus.domain.user.Role

data class UserPrincipal private constructor(
    val userId: Long,
    val email: String,
    val role: Role
) {
    companion object {
        fun fromRequest(userId: Long, email: String, role: Role): UserPrincipal {
            return UserPrincipal(userId = userId, email = email, role = role)
        }
    }
}
