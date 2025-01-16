package com.freedrawing.springplus.domain.todo.dto.response

import com.freedrawing.springplus.domain.todo.entity.Todo
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto

data class AddTodoResponseDto(
    val todoId: Long,
    val title: String,
    val content: String,
    val weather: String,
    val userResponse: UserResponseDto
) {
    companion object {
        fun from(todo: Todo, userResponseDto: UserResponseDto): AddTodoResponseDto {
            return AddTodoResponseDto(
                todoId = todo.id!!,
                title = todo.title,
                content = todo.content,
                weather = todo.weather,
                userResponse = userResponseDto
            )
        }
    }
}