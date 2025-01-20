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
    var id: Long? = null
        protected set

    @Column(name = "content", nullable = false)
    var content: String = content

    @ManyToOne(fetch = FetchType.LAZY)
//    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    val user: User = user

    @ManyToOne(fetch = FetchType.LAZY)
//    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "todo_id", nullable = false, updatable = false)
    val todo: Todo = todo

}