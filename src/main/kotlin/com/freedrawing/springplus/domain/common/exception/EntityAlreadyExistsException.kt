package com.freedrawing.springplus.domain.common.exception

import com.freedrawing.springplus.config.error.ErrorCode

class EntityAlreadyExistsException : BaseException {

    constructor() : super(ErrorCode.ENTITY_ALREADY_EXISTS)

    constructor(errorCode: ErrorCode) : super(errorCode)
}