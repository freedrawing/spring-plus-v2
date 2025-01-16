package com.freedrawing.springplus.domain.auth.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

data class SignupRequestDto(
    @field:NotNull
    @field:Email
    val email: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    val nickname: String,

    @field:NotBlank
    val userRole: String
)
