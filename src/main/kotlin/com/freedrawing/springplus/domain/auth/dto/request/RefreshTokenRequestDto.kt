package com.freedrawing.springplus.domain.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequestDto(
    @field:NotBlank
    val refreshToken: String
)
