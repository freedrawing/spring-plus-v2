package com.freedrawing.springplus.domain.todomanagement.repository

import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.todo.entity.QTodo.todo
import com.freedrawing.springplus.domain.todomanagement.dto.response.TodoManagementResponseDto
import com.freedrawing.springplus.domain.todomanagement.entity.QTodoManagement.todoManagement
import com.freedrawing.springplus.domain.user.entity.QUser.user
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.querydsl.core.types.Projections.constructor
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager

class TodoManagementRepositoryImpl(
    entityManager: EntityManager
) : TodoManagementRepositoryCustom {

    private val queryFactory: JPAQueryFactory = JPAQueryFactory(entityManager)

    // 조금 더 간명하게
    override fun findAllByTodoIdWithUser(todoId: Long): List<TodoManagementResponseDto> {
        return queryFactory
            .select(
                constructor( // `@QueryProjection`을 하기가 귀찮구만
                    TodoManagementResponseDto::class.java,
                    todoManagement.id,
                    constructor(
                        TodoResponseDto::class.java,
                        todo.id,
                        todo.title,
                        todo.content,
                        todo.weather,
                        constructor(
                            UserResponseDto::class.java,
                            todo.user.id,
                            todo.user.email,
                            todo.user.nickname,
                            todo.user.role,
                            todo.user.profileImgUrl
                        ),
                        todo.createdAt,
                        todo.updatedAt
                    ),
                    constructor(
                        UserResponseDto::class.java,
                        user.id,
                        user.email,
                        user.nickname,
                        user.role,
                        user.profileImgUrl
                    )
                )
            )
            .from(todoManagement)
            .join(todoManagement.manager, user)
            .join(todoManagement.todo, todo)
            .where(todoManagement.todo.id.eq(todoId))
            .fetch()
    }

    override fun existsByTodoIdAndManagerId(todoId: Long, managerId: Long): Boolean {
        return queryFactory
            .selectOne()
            .from(todoManagement)
            .where(
                todoManagement.todo.id.eq(todoId),
                todoManagement.manager.id.eq(managerId)
            )
            .fetchFirst() != null
    }


}