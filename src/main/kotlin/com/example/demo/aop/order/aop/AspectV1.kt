package com.example.demo.aop.order.aop

import com.example.demo.common.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

@Aspect
class AspectV1 {
    private val log = logger()

    @Around("execution(* com.example.demo.aop.order..*(..))")
    fun doLog(jointPoint: ProceedingJoinPoint): Any? {
        log.info("[log] {}", jointPoint.signature)
        return jointPoint.proceed()
    }

}