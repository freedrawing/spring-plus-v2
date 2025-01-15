package com.freedrawing.springplus.domain.user

import java.util.*

enum class Role {
    ADMIN,
    USER,
    ;

    companion object {
        fun of(roleType: String): Role {
            return values().firstOrNull { it.name.equals(roleType, ignoreCase = true)}
                ?: throw RuntimeException()
        }
    }
}