package com.example.demo.aop.order.aop.exam

import com.example.demo.common.logger
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ExamTest {
    private val log = logger()

    @Autowired
    lateinit var examService: ExamService

    @Test
    fun test() {
        for (i in 0..4){
            log.info("client request i={$i}")
            examService.request("data$i")
        }
    }
}