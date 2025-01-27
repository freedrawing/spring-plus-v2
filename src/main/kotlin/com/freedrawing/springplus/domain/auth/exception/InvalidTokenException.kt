package com.freedrawing.springplus.domain.auth.exception

import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.domain.common.exception.InvalidRequestException

class InvalidTokenException : InvalidRequestException {

    constructor() : super(ErrorCode.INVALID_TOKEN)

    constructor(message: String) :super(message)
}