package com.freedrawing.springplus.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.freedrawing.springplus.domain.auth.repository.RefreshTokenRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@EnableMethodSecurity(
    securedEnabled = true,  // @Secured 활성화
    prePostEnabled = true,  // @PreAuthorize, @PostAuthorize 활성화
    jsr250Enabled = true    // @RolesAllowed 활성화
)
@Configuration
class WebSecurityConfig(
    private val tokenProvider: TokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository, // 바로 Repo를 참조하는 게 좋아보이지는 않는다.
    private val objectMapper: ObjectMapper
) {

    // Spring Security 모든 기능 비활성화.
    @Bean
    fun configure(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring().requestMatchers(
                "/",
                "/static/**",
                "/index.html",
                "/*.ico",
                "/auth/**",
                "/health/**",
            )
        }
    }

    // Spring Security의 필터 체인을 구성하는 역할
    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // 세션 필터(?) 비활성화 (Form 로그인 하려면 활성화 해야 함)
            .formLogin { it.disable() } // Form 기반 로그인 비활성화
            .httpBasic { it.disable() } // HTTP Basic 비활성화 (Authorization 헤더에 사용자 이름과 비밀번호 Base64 인코딩하여 인증하는 방식)
            .requestCache { it.disable() } // 요청 캐시 비활성화 (인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 해당 요청 정보 저장 후, 인증 성공 후에 원래 요청했던 리소스 리다이렉팅 하기 위해 사용
            .logout { it.disable() }
            .addFilterBefore(JwtAuthenticationFilter(tokenProvider, refreshTokenRepository), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(FrontExceptionHandlerFilter(objectMapper), JwtAuthenticationFilter::class.java)
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/dummy-path"
                )
                    .permitAll() // 인증 없이 허용
                    .anyRequest().authenticated() // 나머지는 인증 필요
            }
            .exceptionHandling { // 그리 좋은 흐름은 아닌 듯. 사실 여기까지 올까 의문이다.
                it.authenticationEntryPoint(CustomAuthenticationEntryPoint(objectMapper))
                it.accessDeniedHandler(CustomAccessDeniedHandler(objectMapper))
            }
            .build()
    }

    @Bean
    fun bCryptPasswordEncoder() = BCryptPasswordEncoder()
}