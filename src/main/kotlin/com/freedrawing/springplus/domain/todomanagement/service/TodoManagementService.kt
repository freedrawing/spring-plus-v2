package com.freedrawing.springplus.domain.todomanagement.service

import com.freedrawing.springplus.config.error.ErrorCode.TODO_MANAGEMENT_ALREADY_EXISTS
import com.freedrawing.springplus.config.error.ErrorCode.TODO_MANAGEMENT_NOT_FOUND
import com.freedrawing.springplus.domain.common.exception.EntityAlreadyExistsException
import com.freedrawing.springplus.domain.common.exception.InvalidRequestException
import com.freedrawing.springplus.domain.common.exception.NotFoundException
import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.todo.entity.Todo
import com.freedrawing.springplus.domain.todo.service.TodoService
import com.freedrawing.springplus.domain.todomanagement.dto.response.TodoManagementResponseDto
import com.freedrawing.springplus.domain.todomanagement.entity.TodoManagement
import com.freedrawing.springplus.domain.todomanagement.repository.TodoManagementHistoryRepository
import com.freedrawing.springplus.domain.todomanagement.repository.TodoManagementRepository
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.freedrawing.springplus.domain.user.entity.User
import com.freedrawing.springplus.domain.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class TodoManagementService(
    private val todoManagementRepository: TodoManagementRepository,
    private val userService: UserService,
    private val todoService: TodoService,
    private val todoManagementHistoryService: TodoManagementHistoryService,
    private val todoManagementHistoryRepository: TodoManagementHistoryRepository
) {

    // 검증 로직 따로 빼면서, 추가 쿼리 있는지 확인
    @Transactional
    fun assignManagerToTodo(
        assignerId: Long,
        managerId: Long,
        todoId: Long,
    ): TodoManagementResponseDto {

        // Log 찍을려면 어쩔 수 없이 엔티티를 먼저 불러와야 함. 검증 로직을 먼저 수행하면 더 좋았겠지만...
        val assigner = userService.getUserById(assignerId)
        val findManager = userService.getUserById(managerId)
        val findTodo = todoService.getTodoById(todoId)
        val todoOwner = findTodo.user

        try {
            validateManagerAssignment(findTodo, findManager)

            val savedTodoManagement = todoManagementRepository.save(TodoManagement(findManager, findTodo))
            findTodo.addManager(savedTodoManagement) // 굳이 관계를 맺어줄 필요는 없음. 역방향 Cascade 설정을 안 했기에

            // Save log
            todoManagementHistoryService.addTodoManagementHistory(
                todo = findTodo,
                manager = findManager,
                todoOwner = todoOwner,
                assigner = assigner,
                isValidAssignment = true
            )

            return createResponse(
                manager = findManager,
                todo = findTodo,
                todoOwner = todoOwner,
                savedTodoManagement = savedTodoManagement
            )

        } catch (e: Exception) {
            todoManagementHistoryService.addTodoManagementHistory(
                todo = findTodo,
                manager = findManager,
                todoOwner = todoOwner,
                assigner = assigner,
                isValidAssignment = false
            )

            throw e
        }
    }

    private fun createResponse(
        manager: User,
        todo: Todo,
        todoOwner: User,
        savedTodoManagement: TodoManagement
    ): TodoManagementResponseDto {

        val managerResponse = UserResponseDto.fromEntity(manager)
        val todoResponse = TodoResponseDto.from(todo, UserResponseDto.fromEntity(todoOwner))

        return TodoManagementResponseDto(
            todoManagementId = savedTodoManagement.id!!,
            todo = todoResponse,
            manager = managerResponse
        )
    }

    fun validateManagerAssignment(todo: Todo, manager: User) {
        if (todoManagementRepository.existsByTodoIdAndManagerId(todo.id!!, manager.id!!)) {
            throw EntityAlreadyExistsException(TODO_MANAGEMENT_ALREADY_EXISTS)
        }

        if (manager.role.isManager().not()) {
            throw InvalidRequestException("`Manager` 등급만 일정 관리자로 등록할 수 있습니다.")
        }

        if (manager.id == todo.user.id) {
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