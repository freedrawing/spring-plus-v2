package com.freedrawing.springplus.domain.auth.dto.reponse

import com.freedrawing.springplus.domain.user.User

data class SignupResponseDto(
    val userId: Long,
    val email: String,
    val userRole: String
) {
    companion object {
        fun fromEntity(user: User): SignupResponseDto {
            return SignupResponseDto(userId = user.id!!, email = user.email, userRole = user.role.name)
        }
    }
}
