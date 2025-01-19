package com.freedrawing.springplus.config.error

import com.freedrawing.springplus.config.util.LoggerUtil
import com.freedrawing.springplus.domain.common.exception.BaseException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestValueException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.stream.Collectors

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {

        LoggerUtil.logger.error { "${"MethodArgumentNotValidException msg: {}"} ${e.body}" }

        val errorMessages = e.bindingResult.fieldErrors
            .stream()
            .map { String.format("%s: %s", it.field, it.defaultMessage) }
            .collect(Collectors.joining("\n"))

        return createErrorResponseEntity(ErrorCode.INVALID_REQUEST, errorMessages)
    }

    // 잘못된 HttpMethod 요청 왔을 때 (컨트롤러에서 정의되지 않은 Http 메서드 요청할 때)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorResponse> {
        LoggerUtil.logger.error(e) { "${"HttpRequestMethodNotSupportedException"}" }
        return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED)
    }

    // 파라미터, 혹은, Body에 잘못된 값이 들어왔을 때
    @ExceptionHandler(
        MethodArgumentTypeMismatchException::class,
        HttpMessageConversionException::class,
        MissingRequestValueException::class
    )
    protected fun handleInvalidInputException(e: RuntimeException): ResponseEntity<ErrorResponse> {
        LoggerUtil.logger.error(e) { "${"Invalid Input Exception"}" }
        return createErrorResponseEntity(ErrorCode.INVALID_REQUEST)
    }

    // Business Exception(대부분 여기서 걸림)
    @ExceptionHandler(BaseException::class)
    protected fun handleBusinessBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        LoggerUtil.logger.error(e) { "${"BusinessBaseException"}" }
        return createErrorResponseEntity(e.errorCode, e.message)
    }

    // 500 서버 에러 정도만 여기서 걸림
    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        LoggerUtil.logger.error(e) { "${"Exception"}" }
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    private fun createErrorResponseEntity(
        errorCode: ErrorCode,
        message: String? = null
    ): ResponseEntity<ErrorResponse> {

        val errorResponse =
            if (message.isNullOrEmpty()) ErrorResponse.of(errorCode)
            else ErrorResponse.of(errorCode, message)

        return ResponseEntity(errorResponse, errorCode.status)
    }
}