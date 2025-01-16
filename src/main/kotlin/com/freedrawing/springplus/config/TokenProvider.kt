package com.freedrawing.springplus.config

import com.freedrawing.springplus.config.util.Token.ACCESS_TOKEN_TYPE
import com.freedrawing.springplus.config.util.Token.BEARER_PREFIX
import com.freedrawing.springplus.config.util.Token.REFRESH_TOKEN_TYPE
import com.freedrawing.springplus.domain.auth.exception.ExpiredTokenException
import com.freedrawing.springplus.domain.auth.exception.InvalidTokenException
import com.freedrawing.springplus.domain.user.Role
import com.freedrawing.springplus.domain.user.User
import io.jsonwebtoken.*
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service
class TokenProvider(private val jwtProperties: JwtProperties) {

    fun generateToken(user: User, tokenType: String, validPeriod: Duration): String {
        val expiration: Date = Date(Date().time + validPeriod.toMillis())

        return if (tokenType == ACCESS_TOKEN_TYPE)
            makeAccessToken(expiration, user)
        else
            makeRefreshToken(expiration, user)
    }

    private fun makeAccessToken(expiration: Date, user: User): String {
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(jwtProperties.issuer)
            .setIssuedAt(Date())
            .setExpiration(expiration)
            .setSubject(user.id.toString())
            .claim("nickname", user.nickname)
            .claim("role", user.role) // `accessToken`에만 `Role 포함
            .claim("tokenType", ACCESS_TOKEN_TYPE)
            .signWith(SignatureAlgorithm.HS256, jwtProperties.secretKey)
            .compact()
    }

    private fun makeRefreshToken(expiration: Date, user: User): String {
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(jwtProperties.issuer)
            .setIssuedAt(Date())
            .setExpiration(expiration)
            .setSubject(user.id.toString())
            .claim("tokenType", REFRESH_TOKEN_TYPE)
            .signWith(SignatureAlgorithm.HS256, jwtProperties.secretKey)
            .compact()
    }

    fun validateToken(rawToken: String) {
        try {
            val extractedToken = extractToken(rawToken)
            Jwts.parser()
                .setSigningKey(jwtProperties.secretKey) // 비밀키로 복호화
                .parseClaimsJws(extractedToken)
        } catch (e: ExpiredJwtException) {
            throw ExpiredTokenException()
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    fun isRefreshToken(token: String): Boolean {
        val claims = getClaims(token)
        val tokenType = claims.get("tokenType", String::class.java)

        return tokenType == REFRESH_TOKEN_TYPE
    }

    fun getUserIdFrom(token: String): Long {
        try { // 개인적으로 이런 `try-catch`는 좀 별로인 듯...
            val subject = getClaims(token).subject
            return subject.toLong()
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    fun getRoleFrom(token: String): Role {
        try {
            val claims = getClaims(token)
            val roleType = claims.get("role", String::class.java)

            return Role.of(roleType)
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    fun getNicknameFrom(token: String): String {
        try {
            val claims = getClaims(token)
            val nickname = claims.get("nickname", String::class.java)

            return nickname
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    private fun getClaims(token: String): Claims {
        val extractedToken: String = extractToken(token)

        // 여기서 isValidToken()을 한 번 더 사용해야 하나... 크흠...
        return Jwts.parser()
            .setSigningKey(jwtProperties.secretKey)
            .parseClaimsJws(extractedToken)
            .getBody()
    }

    private fun extractToken(rawToken: String?): String { // "Bearer " 접두사 제거

        if (rawToken.isNullOrBlank()) {
            throw InvalidTokenException()
        }

        // Bearer 접두사가 없더라도 우선 허용.
        return if (rawToken.startsWith(BEARER_PREFIX)) rawToken.substring(BEARER_PREFIX.length) else rawToken
    }
}