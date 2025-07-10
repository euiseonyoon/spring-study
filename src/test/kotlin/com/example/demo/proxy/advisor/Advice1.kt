package com.example.demo.proxy.advisor

import com.example.demo.common.logger
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation

class Advice1: MethodInterceptor {
    private val log = logger()

    override fun invoke(invocation: MethodInvocation): Any? {
        log.info("advice1 호출")
        return invocation.proceed()
    }
}