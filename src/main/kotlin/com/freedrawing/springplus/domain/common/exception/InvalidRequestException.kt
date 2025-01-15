package com.freedrawing.springplus.domain.common.exception

import com.freedrawing.springplus.config.error.ErrorCode

open class InvalidRequestException : BaseException {

    constructor() : super(ErrorCode.INVALID_REQUEST)

    constructor(errorCode: ErrorCode) : super(errorCode)
}
