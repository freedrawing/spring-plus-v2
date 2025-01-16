package com.freedrawing.springplus.domain.todo.repository

import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TodoRepositoryCustom {

    fun findAllByOrderByModifiedAtDesc(pageable: Pageable): Page<TodoResponseDto>
}