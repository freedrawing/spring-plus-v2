package com.freedrawing.springplus.domain.comment.controller

import com.freedrawing.springplus.domain.auth.UserPrincipal
import com.freedrawing.springplus.domain.auth.annotation.Authentication
import com.freedrawing.springplus.domain.comment.dto.request.AddCommentRequestDto
import com.freedrawing.springplus.domain.comment.dto.response.CommentResponseDto
import com.freedrawing.springplus.domain.comment.service.CommentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/comments")
    fun addComment(
        @Authentication userPrincipal: UserPrincipal,
        @RequestParam(required = true) todoId: Long,
        @Valid @RequestBody requestDto: AddCommentRequestDto
    ): ResponseEntity<CommentResponseDto> {

        val response = commentService.saveComment(
            userId = userPrincipal.userId,
            todoId = todoId,
            requestDto = requestDto
        )
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping("/comments")
    fun getComments(@RequestParam(required = true) todoId: Long): ResponseEntity<List<CommentResponseDto>> {
        val comments = commentService.getComments(todoId)
        return ResponseEntity(comments, HttpStatus.OK)
    }

}