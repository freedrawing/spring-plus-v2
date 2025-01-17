package com.freedrawing.springplus.domain.todomanagement.dto.response

import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto

data class TodoManagementResponseDto(
    val todoManagementId: Long,
    val todo: TodoResponseDto,
    val manager: UserResponseDto
)
