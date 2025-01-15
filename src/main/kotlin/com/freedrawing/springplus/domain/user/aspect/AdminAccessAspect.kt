package com.freedrawing.springplus.domain.user.aspect

import com.freedrawing.springplus.domain.auth.UserPrincipal
import com.freedrawing.springplus.domain.auth.exception.AuthenticationException
import com.freedrawing.springplus.domain.common.exception.AccessDeniedException
import com.freedrawing.springplus.domain.user.annotation.AdminOnly
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Component
@Aspect
class AdminAccessAspect {

    @Before("@annotation(adminOnly)")
    fun checkAdminAccess(joinPoint: JoinPoint, adminOnly: AdminOnly) {
        val args = joinPoint.args

        // Check for if @Authentication parameter
        var isAuthenticatedUser = false
        var userPrincipal: UserPrincipal? = null

        for (arg in args) {
            if (arg is UserPrincipal) {
                isAuthenticatedUser = true
                userPrincipal = arg
                break
            }
        }

        if (isAuthenticatedUser.not() || userPrincipal == null) {
            throw AuthenticationException("인증된 사용자가 아닙니다.")
        }

        if (userPrincipal.role.isAdmin().not()) {
            throw AccessDeniedException()
        }

    }
}