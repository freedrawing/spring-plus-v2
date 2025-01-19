package com.freedrawing.springplus.config

import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.config.util.LoggerUtil
import com.freedrawing.springplus.config.util.Token
import com.freedrawing.springplus.config.util.Url
import com.freedrawing.springplus.domain.common.exception.NotFoundException
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.StringUtils.hasText

class JwtAuthenticationFilter(
    private val tokenProvider: TokenProvider
) : Filter {

    override fun doFilter(httpRequest: ServletRequest, httpResponse: ServletResponse, filterChain: FilterChain) {

        val httpServletRequest = httpRequest as HttpServletRequest
        val httpServletResponse = httpResponse as HttpServletResponse

        if (Url.isIncludedInWhiteList(httpRequest.requestURI)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse)
            return
        }

        val accessToken = httpServletRequest.getHeader(Token.AUTHORIZATION_HEADER)
        validateToken(accessToken)

        val userId = tokenProvider.getUserIdFrom(accessToken)
        val nickname = tokenProvider.getNicknameFrom(accessToken)
        val role = tokenProvider.getRoleFrom(accessToken)

        httpServletRequest.setAttribute("userId", userId)
        httpServletRequest.setAttribute("nickname", nickname)
        httpServletRequest.setAttribute("role", role)

        filterChain.doFilter(httpServletRequest, httpServletResponse)
    }

    private fun validateToken(accessToken: String?) {
        if (hasText(accessToken).not()) {
            LoggerUtil.logger.error { "Missing JWT token. Sending error response" }
            throw NotFoundException(ErrorCode.TOKEN_NOT_FOUND)
        }

        tokenProvider.validateToken(accessToken!!)
    }

}