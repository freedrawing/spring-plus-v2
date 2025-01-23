package com.freedrawing.springplus.domain.todomanagement.controller

import com.freedrawing.springplus.domain.auth.UserPrincipal
import com.freedrawing.springplus.domain.todomanagement.dto.response.TodoManagementResponseDto
import com.freedrawing.springplus.domain.todomanagement.service.TodoManagementService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class TodoManagementController(
    private val todoManagementService: TodoManagementService
) {

    // `Admin`만 일정 관리 가능
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/todo-managements")
    fun assignManager(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam(required = true) todoId: Long,
        @RequestParam(required = true) managerId: Long
    ): ResponseEntity<TodoManagementResponseDto> {

        val response = todoManagementService.assignManagerToTodo(
            assignerId = userPrincipal.userId,
            managerId = managerId,
            todoId = todoId
        )
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping("/todo-managements")
    fun getManagements(@RequestParam todoId: Long): ResponseEntity<List<TodoManagementResponseDto>> {
        val response = todoManagementService.getAllTodoManagementsByTodoId(todoId)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/todo-managements/{todoManagementId}")
    fun deleteTodoManagement(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable todoManagementId: Long
    ): ResponseEntity<Void> {

        todoManagementService.deleteManager(todoManagementId)
        return ResponseEntity(HttpStatus.OK)
    }


}