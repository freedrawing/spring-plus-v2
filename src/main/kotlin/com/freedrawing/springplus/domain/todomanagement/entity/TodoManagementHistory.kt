package com.freedrawing.springplus.domain.todomanagement.entity

import com.freedrawing.springplus.domain.common.entity.Timestamped
import com.freedrawing.springplus.domain.todo.entity.Todo
import com.freedrawing.springplus.domain.user.entity.User
import jakarta.persistence.*

@Table(name = "todo_management_history")
@Entity
class TodoManagementHistory(
    todo: Todo,
    assigner: User,
    manager: User,
    todoOwner: User,
    isSuccess: Boolean = true
) : Timestamped() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    val id: Long = 0

    @ManyToOne(targetEntity = Todo::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    val todo: Todo = todo

    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "assigner_id", nullable = false)
    val assigner: User = assigner

    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    val manager: User = manager

    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_owner_id", nullable = false)
    val todoOwner: User = todoOwner

    @Column(name = "is_success", nullable = false)
    var isSuccess: Boolean = isSuccess
        protected set

}