package com.freedrawing.springplus.domain.user.entity

import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.domain.common.exception.InvalidRequestException

enum class Role {
    ADMIN,
    MANAGER,
    USER,
    ;

    companion object {
        fun of(role: String): Role {
            return values().firstOrNull { it.name.equals(role, ignoreCase = true) }
                ?: throw InvalidRequestException(ErrorCode.INVALID_ROLE)
        }
    }

    fun isAdmin() = this == ADMIN

    fun isManager() = this == MANAGER

}