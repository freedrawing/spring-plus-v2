package com.freedrawing.springplus.domain.todo.service

import com.freedrawing.springplus.client.WeatherClient
import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.config.error.ErrorCode.*
import com.freedrawing.springplus.domain.common.exception.NotFoundException
import com.freedrawing.springplus.domain.todo.dto.request.AddTodoRequestDto
import com.freedrawing.springplus.domain.todo.dto.response.AddTodoResponseDto
import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.todo.entity.Todo
import com.freedrawing.springplus.domain.todo.repository.TodoRepository
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.freedrawing.springplus.domain.user.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TodoService(
    private val todoRepository: TodoRepository,
    private val userService: UserService,
    private val weatherClient: WeatherClient
) {

    fun saveTodo(userId: Long, requestDto: AddTodoRequestDto): AddTodoResponseDto {
        val findUser = userService.getUserById(userId)
        val todayWeather = weatherClient.todayWeather

        val newTodo = Todo(
            title = requestDto.title,
            content = requestDto.content,
            weather = todayWeather,
            user = findUser
        )

        val savedTodo = todoRepository.save(newTodo)
        val userResponseDto = UserResponseDto.fromEntity(findUser)
        return AddTodoResponseDto.from(savedTodo, userResponseDto)
    }

    fun getAllTodos(pageable: Pageable): Page<TodoResponseDto> {
        return todoRepository.findAllByOrderByModifiedAtDesc(pageable)
    }

    fun getTodoDetailsById(todoId: Long): TodoResponseDto {
        val findTodo = todoRepository.findByIdWithUser(todoId) ?: throw NotFoundException(TODO_NOT_FOUND)
        val userResponse = UserResponseDto.fromEntity(findTodo!!.user)

        // 쿼리 하나 정도 더 나가는 건 괜찮지 않을까? 아닌가..?
//        val findTodo = getTodoById(todoId)
//        val userResponse = UserResponseDto.fromEntity(findTodo.user)

        return TodoResponseDto.from(findTodo, userResponse)
    }

    fun getTodoById(todoId: Long): Todo {
        return todoRepository.findById(todoId)
            .orElseThrow { NotFoundException(TODO_NOT_FOUND) }
    }
}