package com.freedrawing.springplus.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO

@Configuration
@EnableJpaAuditing
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO) // 페이징시 파라미터 간소화
class JpaConfig {

    @Bean
    fun queryFactory(entityManager: EntityManager): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}