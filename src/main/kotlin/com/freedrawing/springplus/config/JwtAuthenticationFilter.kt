package com.freedrawing.springplus.config

import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.config.util.LoggerUtil
import com.freedrawing.springplus.config.util.Token
import com.freedrawing.springplus.domain.auth.repository.RefreshTokenRepository
import com.freedrawing.springplus.domain.auth.service.AuthService
import com.freedrawing.springplus.domain.common.exception.AccessDeniedException
import com.freedrawing.springplus.domain.common.exception.NotFoundException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils.hasText
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val tokenProvider: TokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse

        LoggerUtil.logger.debug { "request.requestURI=${request.requestURI}" }

        val accessToken = httpServletRequest.getHeader(Token.AUTHORIZATION_HEADER)
        validateToken(accessToken)

        val userId = tokenProvider.getUserIdFrom(accessToken)
        if (refreshTokenRepository.existsById(userId).not()) { // 로그아웃한 유저면 로그인 먼저 해야 함.
            throw AccessDeniedException("로그아웃된 유저입니다.")
        }

        val authentication = tokenProvider.getAuthentication(accessToken)
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(httpServletRequest, httpServletResponse)
    }

//    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
//        return Url.isIncludedInWhiteList(request.requestURI)
//    }

    private fun validateToken(accessToken: String?) {
        if (hasText(accessToken).not()) {
            LoggerUtil.logger.error { "Missing JWT token. Sending error response" }
            throw NotFoundException(ErrorCode.TOKEN_NOT_FOUND)
        }

        tokenProvider.validateToken(accessToken!!)
    }

}