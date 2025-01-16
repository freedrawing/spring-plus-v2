package com.freedrawing.springplus.domain.todo.dto.request

import jakarta.validation.constraints.NotBlank

data class AddTodoRequestDto(
    @field:NotBlank
    val title: String,

    @field:NotBlank
    val content: String
)
