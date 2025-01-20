package com.freedrawing.springplus.domain.todo.entity

import com.freedrawing.springplus.domain.comment.entity.Comment
import com.freedrawing.springplus.domain.common.entity.Timestamped
import com.freedrawing.springplus.domain.todomanagement.entity.TodoManagement
import com.freedrawing.springplus.domain.user.User
import jakarta.persistence.*


@Entity
@Table(name = "todo")
class Todo(
    title: String,
    content: String,
    weather: String,
    user: User
) : Timestamped() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    var id: Long? = null
        protected set

    @Column(name = "title", nullable = false, updatable = false)
    var title: String = title
        protected set

    @Column(name = "content", nullable = false)
    var content: String = content
        protected set

    @Column(name = "weather", nullable = false)
    var weather: String = weather
        protected set

    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User = user

    @OneToMany(
        mappedBy = "todo",
        targetEntity = TodoManagement::class,
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST],
        orphanRemoval = true
    )
    private val _todoManagements: MutableList<TodoManagement> = mutableListOf()
    val todoManagements: List<TodoManagement> get() = _todoManagements

    @OneToMany(
        mappedBy = "todo",
        targetEntity = Comment::class,
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST],
        orphanRemoval = true
    )
    private val _comments: MutableList<Comment> = mutableListOf()
    val comments: List<Comment> get() = _comments

    fun addManager(todoManagement: TodoManagement) {
        _todoManagements.add(todoManagement)
    }

    fun addComments(comment: Comment) {
        _comments.add(comment)
    }

    fun addComments(comments: List<Comment>) {
        _comments.addAll(comments)
    }

}