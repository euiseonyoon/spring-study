package com.example.demo.aop.order.aop.exam.aspect

import com.example.demo.aop.order.aop.exam.annotation.Retry
import com.example.demo.common.logger
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

@Aspect
class RetryAspect {
    private val log = logger()

    // NOTE: Retry를 받고 싶으면
    @Around("@annotation(retryAnnotation)")
    fun doAtTraceAnnotation(joinPoint: ProceedingJoinPoint, retryAnnotation: Retry): Any? {

        log.info("[Retry] {} retry={}", joinPoint.signature, retryAnnotation)
        val maxRetry = retryAnnotation.maxRetry
        var exceptionHolder: Exception? = null
        for(i in 0 .. maxRetry - 1 ) {
            log.info("[Retry] count={}/{}", i + 1, maxRetry)
            try {
                return joinPoint.proceed()
            } catch (e: Exception) {
                exceptionHolder = e
            }
        }
        throw exceptionHolder!!
    }
}
