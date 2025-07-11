package com.example.demo.aop.order.aop.exam.aspect

import com.example.demo.common.logger
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

@Aspect
class TraceAspect {
    private val log = logger()

    private fun makeFullMethodDescription(joinPoint: JoinPoint): String {
        joinPoint
        val simpleDeclaringType = joinPoint.signature.declaringType.simpleName
        val methodName = joinPoint.signature.name

        val args = joinPoint.args
        val argsTypes = args.joinToString(",") { it.javaClass.simpleName }

        return simpleDeclaringType + "." + methodName + "(${argsTypes})"
    }

    @Before("@annotation(com.example.demo.aop.order.aop.exam.annotation.Trace)")
    fun doAtTraceAnnotation(joinPoint: JoinPoint) {
        val fullMethodName = makeFullMethodDescription(joinPoint)
        log.info("[@Trace] Requested. methodName={}, args={}", fullMethodName, joinPoint.args)
    }

    @AfterThrowing("@annotation(com.example.demo.aop.order.aop.exam.annotation.Trace)", throwing = "ex")
    fun doAtException(joinPoint: JoinPoint, ex: Exception) {
        val fullMethodName = makeFullMethodDescription(joinPoint)
        val errorMessage = "${ex.javaClass}: ${ex.message}"

        log.info("[@Trace] Error. methodName={}, args={}, error={}", fullMethodName, joinPoint.args, errorMessage)
    }
}
