package com.freedrawing.springplus.domain.todo.repository

import com.freedrawing.springplus.domain.todo.entity.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo, Long>, TodoRepositoryCustom {

}