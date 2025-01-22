package com.freedrawing.springplus.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.config.error.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.nio.charset.StandardCharsets

class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
): AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()

        val errorResponse = ErrorResponse.of(ErrorCode.AUTHENTICATION_FAILURE)
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}