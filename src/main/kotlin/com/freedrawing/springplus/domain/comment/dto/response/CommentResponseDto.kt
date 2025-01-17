package com.freedrawing.springplus.domain.comment.dto.response

import com.freedrawing.springplus.domain.comment.entity.Comment
import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto

data class CommentResponseDto(
    val commentId: Long,
    val content: String,
    val todo: TodoResponseDto,
    val writer: UserResponseDto
) {
    companion object {
        fun from(
            comment: Comment,
            todoResponse: TodoResponseDto,
            writeResponse: UserResponseDto
        ): CommentResponseDto {
            return CommentResponseDto(
                commentId = comment.id!!,
                content = comment.content,
                todo = todoResponse,
                writer = writeResponse
            )
        }
    }
}
