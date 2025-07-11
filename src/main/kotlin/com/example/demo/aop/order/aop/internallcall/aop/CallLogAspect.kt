package com.example.demo.aop.order.aop.internallcall.aop

import com.example.demo.common.logger
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.JoinPoint

@Aspect
class CallLogAspect {
    private val log = logger()

    @Before("execution(* com.example.demo.aop.order.aop.internallcall..*.*(..))")
    fun doLog(joinPoint: JoinPoint) {
        log.info("AOP적용됨. aop={}", joinPoint.signature)
    }
}