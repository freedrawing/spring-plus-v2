package com.freedrawing.springplus.domain.todo.repository

import com.freedrawing.springplus.domain.todo.entity.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo, Long>, TodoRepositoryCustom {

//    @Query("select t from Todo t join fetch t.user where t.id = :todoId")
//    fun findByIdWithUser(@Param("todoId") todoId: Long): Todo?
}