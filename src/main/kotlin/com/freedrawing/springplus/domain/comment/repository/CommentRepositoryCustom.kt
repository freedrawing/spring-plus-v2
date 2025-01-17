package com.freedrawing.springplus.domain.comment.repository

import com.freedrawing.springplus.domain.comment.dto.response.CommentResponseDto

interface CommentRepositoryCustom {
    fun findAllByTodoIdWithUser(todoId: Long): List<CommentResponseDto>
}