package com.freedrawing.springplus.domain.auth.exception

import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.domain.common.exception.BaseException

class AuthenticationException : BaseException {

    constructor(message: String) : super(message, ErrorCode.AUTHENTICATION_FAILURE)

    constructor(errorCode: ErrorCode) : super(errorCode)

    constructor(message: String, errorCode: ErrorCode) : super(message, errorCode)

    constructor() : super(ErrorCode.AUTHENTICATION_FAILURE)
}