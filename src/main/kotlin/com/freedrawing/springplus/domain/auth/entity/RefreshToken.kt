package com.freedrawing.springplus.domain.auth.entity

import com.freedrawing.springplus.domain.auth.exception.InvalidTokenException
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.time.Duration

@RedisHash(value = "token")
class RefreshToken(
    @Id val userId: Long,
    private val refreshToken: String,
    @TimeToLive private val timeToLive: Long
) {

    constructor(userId: Long, token: String, duration: Duration) : this(userId, token, duration.seconds)


    fun validateMyRefreshToken(token: String) {
        if (refreshToken != token) {
            throw InvalidTokenException("해당 사용자의 Refresh Token이 아닙니다.")
        }
    }
}