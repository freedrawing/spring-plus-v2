package com.freedrawing.springplus.domain.todomanagement.repository

import com.freedrawing.springplus.config.JpaConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@Import(JpaConfig::class)
@DataJpaTest
class TodoManagementRepositoryTest {

    @Autowired
    lateinit var todoManagementRepository: TodoManagementRepository

    @Test
    fun existsTest() {

    }
}