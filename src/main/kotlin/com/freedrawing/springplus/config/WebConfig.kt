package com.freedrawing.springplus.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.Filter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val tokenProvider: TokenProvider
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(AuthUserArgumentResolver())
    }

    @Bean
    fun exceptionHandlerFilter(): FilterRegistrationBean<*> {
        val exceptionHandlerFilterRegisterBean = FilterRegistrationBean<Filter>()

        exceptionHandlerFilterRegisterBean.filter = ExceptionHandlerFilter(ObjectMapper())
        exceptionHandlerFilterRegisterBean.order = 1
        exceptionHandlerFilterRegisterBean.addUrlPatterns("/*")

        return exceptionHandlerFilterRegisterBean
    }

    @Bean
    fun jwtAuthenticationFilter(): FilterRegistrationBean<*> {
        val jwtFilterRegisterBean = FilterRegistrationBean<Filter>()

        jwtFilterRegisterBean.filter = JwtAuthenticationFilter(tokenProvider)
        jwtFilterRegisterBean.order = 2
        jwtFilterRegisterBean.addUrlPatterns("/*")

        return jwtFilterRegisterBean
    }

}