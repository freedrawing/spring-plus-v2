package com.freedrawing.springplus.domain.common.exception

import com.freedrawing.springplus.config.error.ErrorCode

class AccessDeniedException : BaseException {

    constructor(errorCode: ErrorCode) : super(errorCode)

    constructor() : super(ErrorCode.ACCESS_DENIED)

    constructor(message: String, errorCode: ErrorCode) : super(message, errorCode)

    constructor(message: String) : super(message, ErrorCode.ACCESS_DENIED)
}