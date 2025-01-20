package com.freedrawing.springplus.domain.user.dto.response

import com.freedrawing.springplus.domain.user.entity.Role
import com.freedrawing.springplus.domain.user.entity.User

data class UserResponseDto(
    val userId: Long,
    val email: String,
    val nickname: String,
    val role: Role,
    val profileImgUrl: String?
) {
    companion object {
        fun fromEntity(user: User) = UserResponseDto(
            userId = user.id!!,
            email = user.email,
            nickname = user.nickname,
            profileImgUrl = user.profileImgUrl,
            role = user.role
        )

    }
}
