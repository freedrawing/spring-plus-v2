package com.freedrawing.springplus.domain.todomanagement.service

import com.freedrawing.springplus.config.error.ErrorCode.TODO_MANAGEMENT_ALREADY_EXISTS
import com.freedrawing.springplus.config.error.ErrorCode.TODO_MANAGEMENT_NOT_FOUND
import com.freedrawing.springplus.domain.common.exception.EntityAlreadyExistsException
import com.freedrawing.springplus.domain.common.exception.InvalidRequestException
import com.freedrawing.springplus.domain.common.exception.NotFoundException
import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.todo.service.TodoService
import com.freedrawing.springplus.domain.todomanagement.dto.response.TodoManagementResponseDto
import com.freedrawing.springplus.domain.todomanagement.entity.TodoManagement
import com.freedrawing.springplus.domain.todomanagement.repository.TodoManagementRepository
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.freedrawing.springplus.domain.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TodoManagementService(
    private val todoManagementRepository: TodoManagementRepository,
    private val userService: UserService,
    private val todoService: TodoService
) {

    // 검증 로직 따로 빼면서, 추가 쿼리 있는지 확인
    @Transactional
    fun assignManagerToTodo(
        managerId: Long,
        todoId: Long,
    ): TodoManagementResponseDto {

        validateManagerAssignment(managerId = managerId, todoId = todoId)

        val findManager = userService.getUserById(managerId)
        val findTodo = todoService.getTodoById(todoId)
        val todoOwner = findTodo.user

        val savedTodoManagement = todoManagementRepository.save(TodoManagement(findManager, findTodo))
        findTodo.addManager(savedTodoManagement)

        val managerResponse = UserResponseDto.fromEntity(findManager)
        val todoResponse = TodoResponseDto.from(findTodo, UserResponseDto.fromEntity(todoOwner))

        return TodoManagementResponseDto(
            todoManagementId = savedTodoManagement.id!!,
            todo = todoResponse,
            manager = managerResponse
        )
    }

    @Transactional
    fun validateManagerAssignment(todoId: Long, managerId: Long) {
        if (todoManagementRepository.existsByTodoIdAndManagerId(todoId, managerId)) { // 이미 동일 매니저에게 할당된 일정인지
            throw EntityAlreadyExistsException(TODO_MANAGEMENT_ALREADY_EXISTS)
        }

        val findManager = userService.getUserById(managerId) // manager
        if (findManager.role.isManager().not()) {
            throw InvalidRequestException("`Manager` 등급만 일정 관리자로 등록할 수 있습니다.")
        }

        val findTodo = todoService.getTodoById(todoId)
        if (findManager.id == findTodo.user.id) {
            throw InvalidRequestException("일정 작성자는 본인을 담당자로 등록할 수 없습니다.")
        }

    }

    fun getAllTodoManagementsByTodoId(todoId: Long): List<TodoManagementResponseDto> {
        return todoManagementRepository.findAllByTodoIdWithUser(todoId)
    }

    @Transactional
    fun deleteManager(todoManagementId: Long) {
        val findTodoManagement = getTodoManagementById(todoManagementId)
        todoManagementRepository.delete(findTodoManagement)
    }

    fun getTodoManagementById(todoManagementId: Long): TodoManagement {
        return todoManagementRepository.findById(todoManagementId)
            .orElseThrow { NotFoundException(TODO_MANAGEMENT_NOT_FOUND) }
    }

}