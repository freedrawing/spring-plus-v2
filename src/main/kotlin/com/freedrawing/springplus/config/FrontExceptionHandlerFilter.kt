package com.freedrawing.springplus.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.config.error.ErrorCode.*
import com.freedrawing.springplus.config.error.ErrorResponse
import com.freedrawing.springplus.config.util.LoggerUtil
import com.freedrawing.springplus.domain.common.exception.BaseException
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import java.nio.charset.StandardCharsets

class FrontExceptionHandlerFilter(
    private val objectMapper: ObjectMapper
) : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
        val httpServletResponse = response as HttpServletResponse

        try {
            filterChain.doFilter(request, response)
        } catch (e: BaseException) {
            sendErrorResponse(httpServletResponse, e.errorCode, e.message!!)
            LoggerUtil.logger.error(e) { "${"error={}"}" }
        } catch (e: Exception) { // 흠... 기껏해야 인증 에러일 텐데, 500 에러를 넘기는 게 맞나...
            sendErrorResponse(httpServletResponse, INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.message)
            LoggerUtil.logger.error(e) { "${"error={}"}" }
        }
    }

    private fun sendErrorResponse(response: HttpServletResponse, errorCode: ErrorCode, errorMessage: String) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()
        val errorResponse = ErrorResponse.of(errorCode, errorMessage)

        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}