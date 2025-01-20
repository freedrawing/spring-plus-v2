package com.freedrawing.springplus.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.freedrawing.springplus.config.error.ErrorCode
import com.freedrawing.springplus.config.error.ErrorResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.nio.charset.StandardCharsets


@Configuration
class WebSecurityConfig(
    private val tokenProvider: TokenProvider,
    private val objectMapper: ObjectMapper
) {

    // Spring Security 모든 기능 비활성화, 즉, 인증, 인가 서비스를 모든 곳에 적용 X
    @Bean
    fun configure(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring().requestMatchers(
                AntPathRequestMatcher("/auth/**"),
            )
        }
    }

    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // 세션 필터(?) 비활성화
            .formLogin { it.disable() } // Form 기반 로그인 비활성화
            .httpBasic { it.disable() } // HTTP Basic 비활성화 (Authorization 헤더에 사용자 이름과 비밀번호 Base64 인코딩하여 인증하는 방식)
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests {
                it.requestMatchers(
                    AntPathRequestMatcher("/auth/*"),
                    AntPathRequestMatcher("/error/**")
                )
                    .permitAll() // 인증 없이 허용
                    .anyRequest().authenticated() // 나머지는 인증 필요
            }
            .build()



//            .exceptionHandling {
//                it.authenticationEntryPoint { _, response, exception ->
////                    LoggerUtil.logger.error(exception) { "${"error={}"}" }
//
//                    if (exception is BaseException) {
//                        sendErrorResponse(
//                            response = response,
//                            errorCode = exception.errorCode,
//                            errorMessage = exception.message!!
//                        )
//                    } else {
//                        sendErrorResponse(response, INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.message)
//                    }
//                }
//            }

    }

    private fun sendErrorResponse(response: HttpServletResponse, errorCode: ErrorCode, errorMessage: String) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()

        val errorResponse = ErrorResponse.of(errorCode, errorMessage)
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }

    @Bean
    fun jwtAuthenticationFilter() = JwtAuthenticationFilter(tokenProvider)

    @Bean
    fun bCryptPasswordEncoder() = BCryptPasswordEncoder()

}