package com.freedrawing.springplus.domain.user.dto.response

import com.freedrawing.springplus.domain.user.Role
import com.freedrawing.springplus.domain.user.User

data class UserResponseDto(
    val userId: Long,
    val email: String,
    val role: Role
) {
    companion object {
        fun fromEntity(user: User): UserResponseDto {
            return UserResponseDto(user.id!!, user.email, user.role)
        }
    }
}
