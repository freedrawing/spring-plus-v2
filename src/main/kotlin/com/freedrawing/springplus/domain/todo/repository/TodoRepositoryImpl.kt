package com.freedrawing.springplus.domain.todo.repository

import com.freedrawing.springplus.domain.comment.entity.QComment.comment
import com.freedrawing.springplus.domain.todo.dto.response.ComplexTodoResponseDto
import com.freedrawing.springplus.domain.todo.dto.response.QComplexTodoResponseDto
import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.todo.entity.QTodo.todo
import com.freedrawing.springplus.domain.todo.entity.Todo
import com.freedrawing.springplus.domain.todomanagement.entity.QTodoManagement.todoManagement
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.freedrawing.springplus.domain.user.entity.QUser.user
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.time.LocalDate

class TodoRepositoryImpl(private val queryFactory: JPAQueryFactory) : TodoRepositoryCustom {

    override fun findAllByWeatherAndDateRange(
        pageable: Pageable,
        weather: String?,
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Page<TodoResponseDto> {

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
                        user.role,
                        user.profileImgUrl
                    ),
                    todo.createdAt,
                    todo.updatedAt
                )
            )
            .from(todo)
            .join(todo.user, user)
            .where(
                containsWeatherIgnoreCase(weather),
                startDateGoe(startDate),
                endDateLoe(endDate)
            )
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

    private fun containsWeatherIgnoreCase(weather: String?): BooleanExpression? =
        weather?.let { todo.weather.containsIgnoreCase(it) }

    private fun startDateGoe(startDate: LocalDate?): BooleanExpression? =
        startDate?.let { todo.updatedAt.goe(it.atStartOfDay()) }

    private fun endDateLoe(endDate: LocalDate?): BooleanExpression? =
        endDate?.let { todo.updatedAt.loe(it.atStartOfDay()) }

    override fun findAllByNameAndDateAndManager(
        pageable: Pageable,
        todoTitle: String?,
        createdAt: LocalDate?,
        managerName: String?
    ): Page<ComplexTodoResponseDto> {
        val content = queryFactory
            .select(
                QComplexTodoResponseDto(
                    todo.id,
                    todo.title,
                    todo.user.id,
                    todo.user.nickname,
                    todoManagement.id.countDistinct().intValue(),
                    comment.id.countDistinct().intValue(),
                    todo.createdAt
                )
            )
            .from(todo)
            .join(todo.user, user)
            .leftJoin(todo._todoManagements, todoManagement)
            .leftJoin(todo._comments, comment)
            .where(
                containsTodoNameIgnoreCase(todoTitle),
                createdAtGoe(createdAt),
                containsManagerNameIgnoreCase(managerName)
            )
            .groupBy(todo.id, todo.title, todo.user.id, todo.user.nickname)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(todo.createdAt.desc())
            .fetch()

        val countQuery = queryFactory
            .select(todo.count())
            .from(todo)
            .leftJoin(todo._todoManagements, todoManagement)
            .where(
                containsTodoNameIgnoreCase(todoTitle),
                createdAtGoe(createdAt),
                containsManagerNameIgnoreCase(managerName)
            )

        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchOne() ?: 0L }
    }

    private fun containsTodoNameIgnoreCase(todoTitle: String?): BooleanExpression? =
        todoTitle?.let { todo.title.containsIgnoreCase(todoTitle) }

    private fun createdAtGoe(createdAt: LocalDate?): BooleanExpression? =
        createdAt?.let { todo.createdAt.goe(it.atStartOfDay()) }

    private fun containsManagerNameIgnoreCase(managerName: String?): BooleanExpression? =
        managerName?.let { todoManagement.manager.nickname.containsIgnoreCase(managerName) }

    override fun findByIdWithUser(todoId: Long): Todo? {
        return queryFactory
            .selectFrom(todo)
            .join(todo.user, user).fetchJoin()
            .where(todo.id.eq(todoId))
            .fetchFirst()
    }


}