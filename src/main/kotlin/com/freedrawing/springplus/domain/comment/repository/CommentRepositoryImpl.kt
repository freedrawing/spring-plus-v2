package com.freedrawing.springplus.domain.comment.repository

import com.freedrawing.springplus.domain.comment.dto.response.CommentResponseDto
import com.freedrawing.springplus.domain.comment.entity.QComment.comment
import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.todo.entity.QTodo.todo
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.freedrawing.springplus.domain.user.entity.QUser.user
import com.querydsl.core.types.Projections.constructor
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager

class CommentRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CommentRepositoryCustom {

    override fun findAllByTodoIdWithUser(todoId: Long): List<CommentResponseDto> {
        return queryFactory
            .select(
                constructor(
                    CommentResponseDto::class.java,
                    comment.id,
                    comment.content,
                    constructor(
                        TodoResponseDto::class.java,
                        todo.id,
                        todo.title,
                        todo.content,
                        todo.weather,
                        Expressions.nullExpression(UserResponseDto::class.java), // `owner`는 안 넘겨줌. 또 조인해야 해서
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
            .from(comment)
            .join(comment.todo, todo).on(todo.id.eq(todoId))
            .join(comment.user, user)
            .fetch()
    }

}