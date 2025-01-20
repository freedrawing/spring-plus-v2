package com.freedrawing.springplus.domain.auth.service

import com.freedrawing.springplus.domain.auth.CustomUserDetails
import com.freedrawing.springplus.domain.user.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthDetailsService(
    private val userService: UserService
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val findUser = userService.getUserByEmail(email)
        return CustomUserDetails(findUser)
    }

}