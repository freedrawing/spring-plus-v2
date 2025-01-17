package com.freedrawing.springplus.domain.comment.dto.request

import jakarta.validation.constraints.NotBlank

data class AddCommentRequestDto(
    @field:NotBlank
    val content: String
)
