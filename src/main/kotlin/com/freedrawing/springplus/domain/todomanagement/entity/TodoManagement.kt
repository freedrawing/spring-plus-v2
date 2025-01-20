package com.freedrawing.springplus.domain.todomanagement.entity

import com.freedrawing.springplus.domain.common.entity.Timestamped
import com.freedrawing.springplus.domain.todo.entity.Todo
import com.freedrawing.springplus.domain.user.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "todo_management")
class TodoManagement(
    manager: User,
    todo: Todo
) : Timestamped() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    var id: Long? = null
        protected set

    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val manager: User = manager // 일정을 만든 사람

    @JoinColumn(name = "todo_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val todo: Todo = todo

}