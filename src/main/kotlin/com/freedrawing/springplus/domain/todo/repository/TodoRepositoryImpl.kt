package com.freedrawing.springplus.domain.todo.repository

import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.todo.entity.QTodo.todo
import com.freedrawing.springplus.domain.user.QUser.user
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

class TodoRepositoryImpl(entityManager: EntityManager) : TodoRepositoryCustom {

    private val queryFactory = JPAQueryFactory(entityManager)

    override fun findAllByOrderByModifiedAtDesc(pageable: Pageable): Page<TodoResponseDto> {
        val content = queryFactory
            .select(
                Projections.constructor( // 이렇게 가져오는 건 별로 권장할 만한 방법은 아니지만...
                    TodoResponseDto::class.java,
                    todo.id,
                    todo.title,
                    todo.content,
                    todo.weather,
                    Projections.constructor(
                        UserResponseDto::class.java,
                        user.id,
                        user.email,
                        user.nickname,
                        user.role
                    ),
                    todo.createdAt,
                    todo.updatedAt
                )
            )
            .from(todo)
            .join(todo.user, user)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(todo.updatedAt.desc())
            .fetch()

        val countQuery = queryFactory
            .select(todo.count())
            .from(todo)

        return PageableExecutionUtils.getPage(content, pageable) {
            countQuery.fetchOne() ?: 0L
        }
    }


}