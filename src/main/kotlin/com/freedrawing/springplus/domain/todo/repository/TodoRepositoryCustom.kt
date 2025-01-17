package com.freedrawing.springplus.domain.todo.repository

import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.todo.entity.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param

interface TodoRepositoryCustom {

    fun findAllByOrderByModifiedAtDesc(pageable: Pageable): Page<TodoResponseDto>
    fun findByIdWithUser(@Param("todoId") todoId: Long): Todo?
}