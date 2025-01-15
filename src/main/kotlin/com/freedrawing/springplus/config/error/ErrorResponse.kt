package com.freedrawing.springplus.config.error

import lombok.Getter

@Getter
class ErrorResponse {

    val message: String
    val code: String

    private constructor(errorCode: ErrorCode) {
        this.message = errorCode.message
        this.code = errorCode.code
    }

    private constructor(errorCode: ErrorCode, message: String) {
        this.message = message
        this.code = errorCode.code
    }

    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse {
            return ErrorResponse(errorCode)
        }

        fun of(errorCode: ErrorCode, message: String): ErrorResponse {
            return ErrorResponse(errorCode, message)
        }
    }
}