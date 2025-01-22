package com.freedrawing.springplus.config

import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.config.util.LoggerUtil
import com.freedrawing.springplus.config.util.Token
import com.freedrawing.springplus.domain.common.exception.NotFoundException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils.hasText
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val tokenProvider: TokenProvider
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
        val nickname = tokenProvider.getNicknameFrom(accessToken)
        val role = tokenProvider.getRoleFrom(accessToken)


        httpServletRequest.setAttribute("userId", userId)
        httpServletRequest.setAttribute("nickname", nickname)
        httpServletRequest.setAttribute("role", role)


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