package com.example.demo.config.v6_aop.aspect

import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.advanced.trace.logtrace.models.TraceStatus
import com.example.demo.common.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

/**
 * NOTE:
 * AnnotationAwareAspectJAutoProxyCreator는 두 가지 일을 한다.
 * @Aspect 어노테이션이 붙어있는걸 확인하여 advisor를 만들고
 * 그 advisor의 pointcut의 조건에 부합하는 bean을 target으로 하는 proxy도 만들어준다.
 * */
@Aspect
class LogTraceAspect(
    private val trace: LogTrace
) {
    private val log = logger()

    // advanced/app 하위의 모든 bean의 noLog method를 제외한 모든 method들을 대상으로한 pointcut
    @Around("execution(* com.example.demo.advanced.app..*(..)) && !execution(* com.example.demo.advanced.app..noLog(..))")
    fun execute(joinPoint: ProceedingJoinPoint): Any? {
        // 이게 advice
        var status: TraceStatus? = null

        try {
            // joinPoint.target, joinPoint.args 등등의 정보가 제공됨
            val message = joinPoint.signature.toShortString()

            status = trace.begin(message)

            // 로직 호출
            val result = joinPoint.proceed()

            trace.end(status)
            return result
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e
        }
    }
}