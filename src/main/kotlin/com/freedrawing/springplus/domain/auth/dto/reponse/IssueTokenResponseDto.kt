package com.freedrawing.springplus.domain.auth.dto.reponse

data class IssueTokenResponseDto(
    val accessToken: String,
    val refreshToken: String
)
