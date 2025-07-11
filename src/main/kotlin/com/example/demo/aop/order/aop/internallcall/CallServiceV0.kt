package com.example.demo.aop.order.aop.internallcall

import com.example.demo.common.logger
import org.springframework.stereotype.Component

@Component
class CallServiceV0 {
    private val log = logger()

    fun external() {
        log.info("External 호출")
        internal()
    }

    fun internal() {
        log.info("Internal 호출")
    }
}