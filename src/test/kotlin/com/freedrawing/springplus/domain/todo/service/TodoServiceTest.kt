package com.freedrawing.springplus.domain.todo.service

import com.freedrawing.springplus.config.JpaConfig
import com.freedrawing.springplus.domain.comment.entity.Comment
import com.freedrawing.springplus.domain.comment.repository.CommentRepository
import com.freedrawing.springplus.domain.todo.entity.Todo
import com.freedrawing.springplus.domain.todo.repository.TodoRepository
import com.freedrawing.springplus.domain.user.Role
import com.freedrawing.springplus.domain.user.User
import com.freedrawing.springplus.domain.user.repository.UserRepository
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@Import(JpaConfig::class)
//@DataJpaTest
@SpringBootTest
//@ActiveProfiles("test")
class TodoServiceTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var todoRepository: TodoRepository

    @Autowired
    lateinit var commentRepository: CommentRepository

    @Autowired
    lateinit var entityManager: EntityManager


    // 실제 DB에 들어가는지 확인
    @Test
    fun `부모 엔티티가 삭제되면 자식 엔티티도 삭제된다`() {
        // given
        var savedUser = userRepository.save(User("email", "password", "nickname", Role.USER, "profileImgUrl"))
        var savedTodo = todoRepository.save(Todo("title", "content", "weather", savedUser))

        val comments = listOf(
            Comment(content = "content1", user = savedUser, todo = savedTodo),
            Comment(content = "content2", user = savedUser, todo = savedTodo),
            Comment(content = "content3", user = savedUser, todo = savedTodo),
        )
        savedTodo.addComments(comments)
        commentRepository.saveAll(comments)

//        // when
        todoRepository.delete(savedTodo) // 왜 delete도 안 나가는가..?

//        // then
//        assertThat(todoRepository.count()).isZero()
        assertThat(commentRepository.count()).isZero() // <- 그런데 왜 여기서 TODO가 transient 되었다는 예외가 발생할까? Comment 개수 세는 건데...
    }
}