package com.freedrawing.springplus.domain.user

import com.freedrawing.springplus.config.JpaConfig
import com.freedrawing.springplus.config.util.LoggerUtil
import com.freedrawing.springplus.domain.user.entity.Role
import com.freedrawing.springplus.domain.user.entity.User
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(JpaConfig::class)
class UserTest {

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun createUserTest() {
        val user = User(
            email = "user@email.com",
            role = Role.USER,
            nickname = "hello",
            password = "1234",
            profileImgUrl = null
        )

        entityManager.persist(user)

        LoggerUtil.logger.info { "${"user={}"} $user" }

    }


}
