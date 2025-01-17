package com.freedrawing.springplus.domain.todomanagement.controller

import com.freedrawing.springplus.domain.auth.UserPrincipal
import com.freedrawing.springplus.domain.auth.annotation.Authentication
import com.freedrawing.springplus.domain.todomanagement.dto.response.TodoManagementResponseDto
import com.freedrawing.springplus.domain.todomanagement.service.TodoManagementService
import com.freedrawing.springplus.domain.user.annotation.AdminOnly
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class TodoManagementController(
    private val todoManagementService: TodoManagementService
) {

    // `Admin`만 일정 관리 가능
    @AdminOnly
    @PostMapping("/todo-managements")
    fun assignManager(
        @Authentication userPrincipal: UserPrincipal,
        @RequestParam(required = true) todoId: Long,
        @RequestParam(required = true) managerId: Long
    ): ResponseEntity<TodoManagementResponseDto> {

        val response = todoManagementService.assignManagerToTodo(
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

    @AdminOnly
    @DeleteMapping("/todo-managements/{todoManagementId}")
    fun deleteTodoManagement(
        @Authentication userPrincipal: UserPrincipal,
        @PathVariable todoManagementId: Long
    ): ResponseEntity<Void> {

        todoManagementService.deleteManager(todoManagementId)
        return ResponseEntity(HttpStatus.OK)
    }


}