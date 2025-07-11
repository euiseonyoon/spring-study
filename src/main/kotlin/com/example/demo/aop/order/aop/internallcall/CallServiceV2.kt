package com.example.demo.aop.order.aop.internallcall

import com.example.demo.common.logger
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class CallServiceV2(
    // private val applicationContext: ApplicationContext
    private val objectProvider: ObjectProvider<CallServiceV2>
) {
    private val log = logger()

    fun external() {
        log.info("External 호출")
        // val callServiceV2 = applicationContext.getBean(CallServiceV2::class.java)
        val callServiceV2 = objectProvider.`object`
        callServiceV2.internal()
    }

    fun internal() {
        log.info("Internal 호출")
    }
}
