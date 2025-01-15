package com.freedrawing.springplus.domain.user

import com.freedrawing.springplus.config.TokenProvider
import com.freedrawing.springplus.config.util.LoggerUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RoleTest {

    @Autowired
    lateinit var tokenProvider: TokenProvider

    @Test
    fun roleTest() {
        val accessToken =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJoZWxwQDNtZWFscy5jb20iLCJpYXQiOjE3MzY5MjY0MTAsImV4cCI6MTczNjk0ODAxMCwic3ViIjoiMSIsImVtYWlsIjoidXNlcjEyM0BlbWFpbC5jb20iLCJyb2xlIjoiVVNFUiIsInRva2VuVHlwZSI6ImFjY2VzcyJ9.D-t3x0hO_89RGxcQElj5JYJKsyQNn2e_wx84o4BiQVU"

        val role = tokenProvider.getRoleFrom(accessToken)
        LoggerUtil.log.info("role={}", role)
    }
}