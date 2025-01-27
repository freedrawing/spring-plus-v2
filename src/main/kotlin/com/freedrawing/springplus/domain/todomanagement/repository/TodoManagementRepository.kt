package com.freedrawing.springplus.domain.todomanagement.repository

import com.freedrawing.springplus.domain.todo.entity.Todo
import com.freedrawing.springplus.domain.todomanagement.entity.TodoManagement
import org.springframework.data.jpa.repository.JpaRepository

interface TodoManagementRepository : JpaRepository<TodoManagement, Long>, TodoManagementRepositoryCustom