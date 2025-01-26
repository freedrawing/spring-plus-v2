package com.freedrawing.springplus.domain.todo.repository

import com.freedrawing.springplus.domain.todo.dto.response.ComplexTodoResponseDto
import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.todo.entity.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface TodoRepositoryCustom {

    fun findAllByWeatherAndDateRange(
        pageable: Pageable,
        weather: String?,
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Page<TodoResponseDto>

    fun findAllByNameAndDateAndManager(
        pageable: Pageable,
        todoTitle: String?,
        createdAt: LocalDate?,
        managerName: String?
    ): Page<ComplexTodoResponseDto>

    fun findByIdWithUser(@Param("todoId") todoId: Long): Todo?
}