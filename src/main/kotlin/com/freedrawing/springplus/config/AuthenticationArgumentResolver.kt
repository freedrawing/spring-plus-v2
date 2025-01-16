package com.freedrawing.springplus.config;

import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.domain.auth.UserPrincipal
import com.freedrawing.springplus.domain.auth.annotation.Authentication
import com.freedrawing.springplus.domain.common.exception.InvalidRequestException
import com.freedrawing.springplus.domain.user.Role
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class AuthenticationArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val hasValidAnnotation = parameter.getParameterAnnotation(Authentication::class.java) != null
        val withValidParameterType = parameter.parameterType == UserPrincipal::class.java

        // `@Authentication` should be used with `UserPrincipal`
        if (hasValidAnnotation.not() || withValidParameterType.not()) {
            throw InvalidRequestException(ErrorCode.INVALID_ROLE)
        }

        return true
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.nativeRequest as HttpServletRequest

        val userId = request.getAttribute("userId") as Long
        val nickname = request.getAttribute("nickname").toString()
        val role = Role.of(request.getAttribute("role").toString())

        return UserPrincipal.fromRequest(userId = userId, nickname = nickname, role = role)
    }

}
