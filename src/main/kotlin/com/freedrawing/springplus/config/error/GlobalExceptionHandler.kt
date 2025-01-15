package com.freedrawing.springplus.config.error

import com.freedrawing.springplus.config.util.LoggerUtil
import com.freedrawing.springplus.domain.common.exception.BaseException
import lombok.extern.slf4j.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.stream.Collectors

@Slf4j
@ControllerAdvice
class GlobalExceptionHandler {

    /**
     * 이 예외는 validation 전용 예외라서 비즈니스 예외 처리 클래스에 있는 게 조금 안 어울리기는 함. 크기가 커지면 따로 빼서 관리
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {

        LoggerUtil.run { log.error("MethodArgumentNotValidException msg: {}", e.body) }

        val errorMessages = e.bindingResult.fieldErrors
            .stream()
            .map { error: FieldError -> String.format("%s: %s", error.field, error.defaultMessage) }
            .collect(Collectors.joining("\n"))

        val errorResponse = ErrorResponse.of(ErrorCode.INVALID_REQUEST, errorMessages)

        return ResponseEntity
            .badRequest()
            .body(errorResponse)
    }

    // 잘못된 HttpMethod 요청 왔을 때 (컨트롤러에서 정의되지 않은 Http 메서드 요청할 때)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorResponse> {
        LoggerUtil.log.error("HttpRequestMethodNotSupportedException", e)
        return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED)
    }

    // Business Exception(대부분 여기서 걸림)
    @ExceptionHandler(BaseException::class)
    protected fun handleBusinessBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        LoggerUtil.log.error("BusinessBaseException", e)
        return createErrorResponseEntity(e.errorCode)
    }

    // 500 서버 에러 정도만 여기서 걸림
    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        LoggerUtil.log.error("Exception", e)
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    private fun createErrorResponseEntity(errorCode: ErrorCode): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse.of(errorCode), errorCode.status)
    }
}