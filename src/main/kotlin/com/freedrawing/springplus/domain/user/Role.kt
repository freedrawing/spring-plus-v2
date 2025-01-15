package com.freedrawing.springplus.domain.user

import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.domain.common.exception.InvalidRequestException

enum class Role {
    ADMIN,
    USER,
    ;

    companion object {
        fun of(roleType: String): Role {
            return values().firstOrNull { it.name.equals(roleType, ignoreCase = true) }
                ?: throw InvalidRequestException(ErrorCode.INVALID_ROLE)
        }
    }

    fun isAdmin(): Boolean {
        return this.name.equals(ADMIN.name, ignoreCase = true)
    }
}