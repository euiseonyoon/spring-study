package com.example.demo.proxy.common.advice

import com.example.demo.common.logger
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation

class TimeAdvice : MethodInterceptor{

    private val log = logger()

    override fun invoke(invocation: MethodInvocation): Any? {
        log.info("TimeProxy - TimeAdvice 실행")
        val startTime = System.currentTimeMillis()

        /**
         * 알아서 target을 찾아서 호출을 해준다.
         * target class 정보는 이미 invocation(MethodInvocation)에 이미 있음
         * */
        val result = invocation.proceed()

        val endTime = System.currentTimeMillis()
        val execTime = endTime - startTime

        log.info("execTime={${execTime}}")
        log.info("TimeProxy - TimeAdvice 종료")
        return result
    }
}