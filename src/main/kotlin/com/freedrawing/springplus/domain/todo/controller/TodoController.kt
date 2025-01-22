package com.freedrawing.springplus.domain.todo.controller

import com.freedrawing.springplus.domain.auth.UserPrincipal
import com.freedrawing.springplus.domain.todo.dto.request.AddTodoRequestDto
import com.freedrawing.springplus.domain.todo.dto.response.AddTodoResponseDto
import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.todo.service.TodoService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
class TodoController(
    private val todoService: TodoService
) {

    @PostMapping("/todos")
    fun saveTodo(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @Valid @RequestBody requestDto: AddTodoRequestDto
    ): ResponseEntity<AddTodoResponseDto> {
        val response = todoService.saveTodo(userPrincipal.userId, requestDto)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping("/todos")
    fun getTodos(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) weather: String?,
        @RequestParam(required = false, value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") startDate: LocalDate?,
        @RequestParam(required = true, value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") endDate: LocalDate?
    ): ResponseEntity<Page<TodoResponseDto>> {
        val pageable = PageRequest.of(if (page <= 0) 0 else page - 1, size)

        val response = todoService.getAllTodos(
            pageable = pageable,
            weather = weather,
            startDate = startDate,
            endDate = endDate
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/todos/{todoId}")
    fun getTodo(@PathVariable todoId: Long): ResponseEntity<TodoResponseDto> {
        val response = todoService.getTodoDetailsById(todoId)
        return ResponseEntity(response, HttpStatus.OK)
    }
}