package com.freedrawing.springplus.domain.common.exception

import com.freedrawing.springplus.config.error.ErrorCode

class NotFoundException : BaseException {

    constructor() : super(ErrorCode.NOT_FOUND)

    constructor(errorCode: ErrorCode) : super(errorCode)
}