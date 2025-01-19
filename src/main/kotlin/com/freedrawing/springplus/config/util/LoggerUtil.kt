package com.freedrawing.springplus.config.util

import io.github.oshai.kotlinlogging.KotlinLogging
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LoggerUtil {
    companion object {
        //        val log: Logger = LoggerFactory.getLogger(this::class.java)
        val logger = KotlinLogging.logger {}
    }
}