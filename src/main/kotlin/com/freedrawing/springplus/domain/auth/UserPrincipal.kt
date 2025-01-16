package com.freedrawing.springplus.domain.auth

import com.freedrawing.springplus.domain.user.Role

data class UserPrincipal private constructor(
    val userId: Long,
    val nickname: String,
    val role: Role
) {
    companion object {
        fun fromRequest(userId: Long, nickname: String, role: Role): UserPrincipal {
            return UserPrincipal(userId = userId, nickname = nickname, role = role)
        }
    }
}
