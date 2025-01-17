package com.freedrawing.springplus.domain.comment.service

import com.freedrawing.springplus.domain.comment.dto.request.AddCommentRequestDto
import com.freedrawing.springplus.domain.comment.dto.response.CommentResponseDto
import com.freedrawing.springplus.domain.comment.entity.Comment
import com.freedrawing.springplus.domain.comment.repository.CommentRepository
import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.todo.service.TodoService
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.freedrawing.springplus.domain.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val userService: UserService,
    private val todoService: TodoService,
) {

    // 댓글은 우선, 사용자, 매니저, 관리자 모두 남길 수 있게 하자.
    @Transactional
    fun saveComment(userId: Long, todoId: Long, requestDto: AddCommentRequestDto): CommentResponseDto {
        val findUser = userService.getUserById(userId)
        val findTodo = todoService.getTodoById(todoId)

        val savedComment = commentRepository.save(
            Comment(content = requestDto.content, user = findUser, todo = findTodo)
        )
        val writeResponse = UserResponseDto.fromEntity(findUser)
        val todoResponse = TodoResponseDto.from(findTodo)

        return CommentResponseDto.from(
            comment = savedComment,
            todoResponse = todoResponse,
            writeResponse = writeResponse
        )
    }

    fun getComments(todoId: Long): List<CommentResponseDto> {
        return commentRepository.findAllByTodoIdWithUser(todoId)
    }
}