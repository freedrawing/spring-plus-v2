package com.freedrawing.springplus.domain.todo.dto.response

import com.freedrawing.springplus.domain.todo.entity.Todo
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import java.time.LocalDateTime

data class TodoResponseDto(
    val todoId: Long,
    val title: String,
    val content: String,
    val weather: String,
    val owner: UserResponseDto?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(todo: Todo, userResponseDto: UserResponseDto? = null): TodoResponseDto {
            return TodoResponseDto(
                todoId = todo.id!!,
                title = todo.title,
                content = todo.content,
                weather = todo.weather,
                owner = userResponseDto,
                createdAt = todo.createdAt,
                updatedAt = todo.updatedAt
            )
        }
    }
}
