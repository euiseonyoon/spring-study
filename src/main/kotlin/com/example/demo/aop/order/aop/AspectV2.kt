package com.example.demo.aop.order.aop

import com.example.demo.common.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

@Aspect
class AspectV2 {
    private val log = logger()

    // com.example.demo.aop.order 하위의 모든 Bean의 모든 method에 적용
    @Pointcut("execution(* com.example.demo.aop.order..*(..))")
    private fun allOrder(){} // <- pointcut signature, public으로 한다면 다른 @Aspect에서도 사용 가능하다.

    @Around("allOrder()")
    fun doLog(jointPoint: ProceedingJoinPoint): Any? {
        log.info("[log] {}", jointPoint.signature)
        return jointPoint.proceed()
    }
}
