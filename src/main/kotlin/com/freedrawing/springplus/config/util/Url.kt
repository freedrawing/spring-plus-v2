package com.freedrawing.springplus.config.util

import org.springframework.util.PatternMatchUtils

object Url {
    val WHITE_LIST: Array<String> = arrayOf(
        "/auth/**"
    )

    fun isIncludedInWhiteList(requestUrl: String?): Boolean {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestUrl)
    }
}