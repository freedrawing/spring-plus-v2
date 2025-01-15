package com.freedrawing.springplus.config.util

import org.springframework.util.PatternMatchUtils

object Url {
    private val WHITE_LIST: Array<String> = arrayOf(
        "/signup", "/signup/**", "/login",
        "/oauth/**", "/*.ico", "/refresh-token"
    )

    fun isIncludedInWhiteList(requestUrl: String?): Boolean {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestUrl)
    }
}