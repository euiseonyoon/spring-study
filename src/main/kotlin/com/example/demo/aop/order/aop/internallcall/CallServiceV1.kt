package com.example.demo.aop.order.aop.internallcall

import com.example.demo.common.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CallServiceV1 {
    private val log = logger()

    private var callServiceV1: CallServiceV1? = null

    @Autowired
    fun setCallServiceV1(callServiceV1: CallServiceV1) {
        this.callServiceV1 = callServiceV1
    }

    fun external() {
        log.info("External 호출")
        callServiceV1!!.internal() //외부 메서드 호출
    }

    fun internal() {
        log.info("Internal 호출")
    }
}
