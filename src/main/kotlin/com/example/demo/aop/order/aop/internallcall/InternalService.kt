package com.example.demo.aop.order.aop.internallcall

import com.example.demo.common.logger
import org.springframework.stereotype.Component

@Component
class InternalService {
    private val log = logger()
    fun internal() {
        log.info("Internal 호출")
    }
}