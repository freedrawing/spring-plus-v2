package com.freedrawing.springplus.domain.auth.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SigninRequestDto(
    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    val password: String
)
