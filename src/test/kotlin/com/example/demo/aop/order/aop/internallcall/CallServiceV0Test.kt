package com.example.demo.aop.order.aop.internallcall

import com.example.demo.aop.order.aop.internallcall.aop.CallLogAspect
import com.example.demo.common.logger
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(CallLogAspect::class)
class CallServiceV0Test {
    private val log = logger()

    @Autowired
    lateinit var callServiceV0: CallServiceV0

    @Test
    fun external() {
        log.info("target={}", callServiceV0.javaClass)
        callServiceV0.external()
        /**
         *
         * AOP적용됨. aop=void com.example.demo.aop.order.aop.internallcall.CallServiceV0.external()
         * External 호출
         * Internal 호출 <- internal() 호출 될때 ADVICE동작 안했다.
         *
         * this.internal() 처럼 내부 호출은 proxy를 거치지 않는다 -> 그래서 advice가 적용이 안된다.
         *
         * */
    }

    @Test
    fun internal() {
        log.info("target={}", callServiceV0.javaClass)
        callServiceV0.internal()
        /**
         *
         * AOP적용됨. aop=void com.example.demo.aop.order.aop.internallcall.CallServiceV0.internal()
         * Internal 호출  <-  이렇게 바로 internal()을 하면  advice 작동 했다.
         *
         * */
    }
}
