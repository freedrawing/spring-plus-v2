package com.freedrawing.springplus.domain.user.entity

import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.domain.common.exception.InvalidRequestException

enum class Role {
    ADMIN,
    MANAGER,
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

    fun isManager(): Boolean {
        return this.name.equals(MANAGER.name, ignoreCase = true)
    }
}