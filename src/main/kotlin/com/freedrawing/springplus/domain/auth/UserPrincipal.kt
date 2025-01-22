package com.freedrawing.springplus.domain.auth

import com.freedrawing.springplus.domain.user.entity.Role
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    val userId: Long,
    val nickname: String,
    val role: Role
) : UserDetails {

    override fun getAuthorities(): List<SimpleGrantedAuthority> {
        return listOf(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword() = ""

    override fun getUsername() = nickname

}