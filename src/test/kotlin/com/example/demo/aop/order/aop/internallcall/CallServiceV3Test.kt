package com.example.demo.aop.order.aop.internallcall

import com.example.demo.aop.order.aop.internallcall.aop.CallLogAspect
import com.example.demo.common.logger
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(CallLogAspect::class)
class CallServiceV3Test {
    private val log = logger()

    @Autowired
    lateinit var callServiceV3: CallServiceV3

    @Autowired
    lateinit var internalService: InternalService

    @Test
    fun external() {
        log.info("target={}", callServiceV3.javaClass)
        callServiceV3.external()
        /**
         *
         * AOP적용됨. aop=void com.example.demo.aop.order.aop.internallcall.CallServiceV1.external()
         * External 호출
         * AOP적용됨. aop=void com.example.demo.aop.order.aop.internallcall.CallServiceV1.internal()
         * Internal 호출
         *
         * */
    }

    @Test
    fun internal() {
        log.info("target={}", callServiceV3.javaClass)
        internalService.internal()
        /**
         *
         * AOP적용됨. aop=void com.example.demo.aop.order.aop.internallcall.CallServiceV0.internal()
         * Internal 호출  <-  이렇게 바로 internal()을 하면  advice 작동 했다.
         *
         * */
    }
}
