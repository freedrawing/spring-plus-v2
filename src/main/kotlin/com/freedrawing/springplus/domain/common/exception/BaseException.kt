package com.freedrawing.springplus.domain.common.exception;

import com.freedrawing.springplus.config.error.ErrorCode

open class BaseException : RuntimeException {

    val errorCode: ErrorCode

    constructor(message: String, errorCode: ErrorCode) : super(message) {
        this.errorCode = errorCode
    }

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }
}