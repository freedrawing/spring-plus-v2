package com.freedrawing.springplus.domain.auth.dto.reponse

import com.freedrawing.springplus.domain.user.entity.User

data class SignupResponseDto(
    val userId: Long,
    val email: String,
    val nickname: String,
    val userRole: String,
    val profileImgUrl: String?
) {
    companion object {
        fun fromEntity(user: User): SignupResponseDto = SignupResponseDto(
            userId = user.id!!,
            email = user.email,
            nickname = user.nickname,
            userRole = user.role.name,
            profileImgUrl = user.profileImgUrl
        )

    }
}
