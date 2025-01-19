package com.freedrawing.springplus.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("jwt")
class JwtProperties {
    var issuer: String? = null
    var secretKey: String? = null
}