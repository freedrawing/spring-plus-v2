package com.freedrawing.springplus.domain.auth.exception

import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.domain.common.exception.BaseException

class ExpiredTokenException : BaseException {

    constructor() : super(ErrorCode.TOKEN_EXPIRED)
    constructor(message: String) : super(message, ErrorCode.TOKEN_EXPIRED)
}