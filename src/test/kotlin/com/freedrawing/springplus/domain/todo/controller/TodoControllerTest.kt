package com.freedrawing.springplus.domain.todo.controller

import com.freedrawing.springplus.config.P6SpyConfig
import com.freedrawing.springplus.config.error.ErrorCode.TODO_NOT_FOUND
import com.freedrawing.springplus.domain.auth.UserPrincipal
import com.freedrawing.springplus.domain.common.exception.NotFoundException
import com.freedrawing.springplus.domain.todo.dto.response.TodoResponseDto
import com.freedrawing.springplus.domain.todo.entity.Todo
import com.freedrawing.springplus.domain.todo.service.TodoService
import com.freedrawing.springplus.domain.user.dto.response.UserResponseDto
import com.freedrawing.springplus.domain.user.entity.Role
import com.freedrawing.springplus.domain.user.entity.User
import org.apache.catalina.security.SecurityConfig
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@Import(P6SpyConfig::class)
@ActiveProfiles("test")
@WebMvcTest(
    controllers = [TodoController::class],
    excludeFilters = [
        ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [SecurityConfig::class])
    ]
)
class TodoControllerTest {

    @MockitoBean
    private lateinit var todoService: TodoService

    @Autowired
    lateinit var mockMvc: MockMvc


    @WithMockUser(username = "hello", roles = ["USER"])
    @Test
    fun `todo_단건 조회에 성공한다`() {
        // given
        val userPrincipal = UserPrincipal(1L, "hello", Role.USER)
        val user = User(
            email = "user@email.com",
            password = "123",
            nickname = userPrincipal.nickname,
            role = userPrincipal.role,
            profileImgUrl = "https://~~"
        )
        ReflectionTestUtils.setField(user, "id", 1L)

        val userResponse = UserResponseDto.fromEntity(user)
        val todo = Todo(
            title = "title",
            content = "content",
            weather = "weather",
            user = user
        )
        ReflectionTestUtils.setField(todo, "id", 1L)
        ReflectionTestUtils.setField(todo, "createdAt", LocalDateTime.now())
        ReflectionTestUtils.setField(todo, "updatedAt", LocalDateTime.now())

        val todoResponse = TodoResponseDto.from(todo, userResponse)
        `when`(todoService.getTodoDetailsById(anyLong())).thenReturn(todoResponse)

        // when
        val result = mockMvc.perform(
            get("/todos/{todoId}", todo.id!!)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("$.todoId").value(todo.id))
            .andExpect(jsonPath("$.title").value(todo.title))
            .andDo(print())
    }

    @WithMockUser(username = "hello", roles = ["USER"])
    @Test
    fun `todo 단건 조회 시, 'todo'가 존재하지 않아 예외가 발생한다`() {
        // given
        `when`(todoService.getTodoDetailsById(anyLong())).thenThrow(NotFoundException(TODO_NOT_FOUND))

        // when
        val result = mockMvc.perform(
            get("/todos/{todoId}", anyLong())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value(TODO_NOT_FOUND.message))
            .andExpect(jsonPath("$.code").value(TODO_NOT_FOUND.code))
            .andDo(print())
    }
}