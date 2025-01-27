package com.freedrawing.springplus.index

import com.freedrawing.springplus.domain.user.entity.Role
import com.freedrawing.springplus.domain.user.entity.User
import com.github.javafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

@ComponentScan(
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            pattern = ["com.freedrawing.springplus.aws.*", "com.freedrawing.springplus.config.*"]
        )
    ]
)
@SpringBootTest
@ActiveProfiles("test")
class InsertUserTest {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun insertUserDate() {

        val faker = Faker(Locale("en"))
        var users = mutableListOf<User>()
        val password = "\$2a\$10\$7DEBqetiplxmeL8RMkK2A.0vya5hAdW7Fg/GNwXlDgy33aL8I32nO"

        for (i in 1..1000000) {
            val firstName = faker.name().firstName()
            val lastName = faker.name().lastName()

            val email = String.format("%s_%s_%d@test.com", firstName, lastName)
            users.add(User(email, password, "$firstName $lastName", Role.USER, "https://${faker.lorem().words()}"))

            if (users.size == 10000) {
                bulkInsert(users)
                users = mutableListOf()
            }
        }
    }

    fun bulkInsert(users: List<User>) {
        jdbcTemplate.batchUpdate(
            "INSERT INTO users(email ,nickname, password, profile_img_url, role, created_at, updated_at) VALUES (?,?,?,?,?,?,?)",
            users,
            users.size
        ) { ps, user ->
            ps.setString(1, user.email)
            ps.setString(2, user.nickname)
            ps.setString(3, user.password)
            ps.setString(4, user.profileImgUrl)
            ps.setString(5, user.role.name)
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()))
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()))
        }
    }
}