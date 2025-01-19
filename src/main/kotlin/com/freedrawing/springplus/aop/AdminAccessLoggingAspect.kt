package com.freedrawing.springplus.aop

import com.freedrawing.springplus.config.util.LoggerUtil
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime

@Component
@Aspect
class AdminAccessLoggingAspect {

    //    @Before("execution(* com.freedrawing.springplus.domain.user.controller..*(..))")
    @Before("execution(* com.freedrawing.springplus.domain.user.controller.UserController.changeRole(..))")
    fun logBeforeChangeRole(joinPoint: JoinPoint) {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val userId = request.getAttribute("userId") ?: "Unknown"
        val requestUrl = request.requestURI
        val requestTime = LocalDateTime.now()

        LoggerUtil.logger.info {
            "${"[Admin Access] User ID: {}, Time: {}, URL: {}, Method: {}"} ${
                arrayOf(
                    userId,
                    requestTime,
                    requestUrl,
                    joinPoint.signature.name
                )
            }"
        }
    }
}