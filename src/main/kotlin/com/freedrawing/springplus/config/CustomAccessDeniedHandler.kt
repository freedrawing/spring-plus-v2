package com.freedrawing.springplus.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.config.error.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import java.nio.charset.StandardCharsets

class CustomAccessDeniedHandler(
    private val objectMapper: ObjectMapper
): AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()
        val errorResponse = ErrorResponse.of(ErrorCode.ACCESS_DENIED)

        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}