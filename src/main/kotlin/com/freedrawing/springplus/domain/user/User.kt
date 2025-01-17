package com.freedrawing.springplus.domain.user

import com.freedrawing.springplus.domain.common.entity.Timestamped
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    email: String,
    password: String,
    nickname: String,
    role: Role,
    profileImgUrl: String?
) : Timestamped() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    var id: Long? = null
        protected set

    @Column(name = "email", updatable = false, nullable = false, unique = true)
    var email: String = email
        protected set

    @Column(name = "nickname", nullable = false)
    var nickname: String = nickname
        protected set

    @Column(name = "password", nullable = false)
    var password: String = password
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: Role = role
        protected set

    @Column(name = "profile_img_url")
    var profileImgUrl: String? = profileImgUrl

    fun changePassword(newPassword: String) {
        this.password = newPassword
    }

    fun changeRole(newRole: Role) {
        this.role = newRole
    }
}