package com.freedrawing.springplus.domain.auth.dto.reponse

data class SigninResponseDto(
    val accessToken: String,
    val refreshToken: String
)
