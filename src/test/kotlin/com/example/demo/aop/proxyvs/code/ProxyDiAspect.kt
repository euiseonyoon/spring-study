package com.example.demo.aop.proxyvs.code

import com.example.demo.common.logger
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

@Aspect
class ProxyDiAspect {

    private val log = logger()

    @Before("execution(* com.example.demo.aop..*.*(..))")
    fun doTrace(joinPoint: JoinPoint) {
        log.info("[proxyDiAdvice] {}", joinPoint.signature)
    }
}