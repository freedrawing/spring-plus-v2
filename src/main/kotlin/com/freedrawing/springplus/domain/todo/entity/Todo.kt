package com.freedrawing.springplus.domain.todo.entity

import com.freedrawing.springplus.domain.common.entity.Timestamped
import com.freedrawing.springplus.domain.manager.entity.Manager
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

    @OneToMany(mappedBy = "todo")
    val managers: MutableList<Manager> = mutableListOf()

    fun addManager(manager: Manager) {
        managers.add(manager)
    }

}