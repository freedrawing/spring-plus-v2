package com.freedrawing.springplus.config.util

import java.time.Duration

object Token {
    const val ACCESS_TOKEN_TYPE = "access"
    const val REFRESH_TOKEN_TYPE = "refresh"

    const val AUTHORIZATION_HEADER = "Authorization"
    const val BEARER_PREFIX = "Bearer "

    val ACCESS_TOKEN_DURATION: Duration = Duration.ofHours(6)
    val REFRESH_TOKEN_DURATION: Duration = Duration.ofDays(7)

}