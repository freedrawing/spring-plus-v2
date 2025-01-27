package com.freedrawing.springplus.domain.comment.entity

import com.freedrawing.springplus.domain.common.entity.Timestamped
import com.freedrawing.springplus.domain.todo.entity.Todo
import com.freedrawing.springplus.domain.user.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "comment")
class Comment(
    content: String,
    user: User,
    todo: Todo
) : Timestamped() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    val id: Long = 0

    @Column(name = "content", nullable = false)
    var content: String = content

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    val user: User = user

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false, updatable = false)
    val todo: Todo = todo

}