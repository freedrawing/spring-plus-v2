package com.freedrawing.springplus.domain.todo.dto.response

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate
import java.time.LocalDateTime

//data class ComplexTodoResponseDto(
//    val todoId: Long,
//    val todoTitle: String,
//    val ownerId: Long,
//    val ownerName: String,
//    val managerNum: Int,
//    val commentNum: Int
//)

data class ComplexTodoResponseDto @QueryProjection constructor(
    val todoId: Long,
    val todoTitle: String,
    val ownerId: Long,
    val ownerNickname: String,
    val managerNum: Int,
    val commentNum: Int,
    val createdAt: LocalDateTime
)
