package com.freedrawing.springplus.domain.todomanagement.repository

import com.freedrawing.springplus.domain.todomanagement.dto.response.TodoManagementResponseDto

interface TodoManagementRepositoryCustom {

    fun findAllByTodoIdWithUser(todoId: Long): List<TodoManagementResponseDto>
    fun existsByTodoIdAndManagerId(todoId: Long, managerId: Long): Boolean
}