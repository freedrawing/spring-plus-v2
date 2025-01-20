package com.freedrawing.springplus.domain.auth

import com.freedrawing.springplus.domain.user.entity.User
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val user: User) : UserDetails {

    override fun getAuthorities(): List<SimpleGrantedAuthority> {
        return listOf(SimpleGrantedAuthority(user.role.name))
    }

    override fun getPassword() = user.password

    override fun getUsername() = user.email

}