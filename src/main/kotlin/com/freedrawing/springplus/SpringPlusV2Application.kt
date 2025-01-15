package com.freedrawing.springplus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

//@EnableJpaAuditing
@SpringBootApplication
class SpringPlusV2Application

fun main(args: Array<String>) {
    runApplication<SpringPlusV2Application>(*args)
}
