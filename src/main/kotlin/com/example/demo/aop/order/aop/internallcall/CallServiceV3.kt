package com.example.demo.aop.order.aop.internallcall

import com.example.demo.common.logger
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

// 구조를 변경/분리 하는게 Best practice
@Component
class CallServiceV3(
    private val internalService: InternalService,
) {
    private val log = logger()

    fun external() {
        log.info("External 호출")
        internalService.internal()
    }
}
