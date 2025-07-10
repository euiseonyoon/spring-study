package com.example.demo.config.v3_proxy_factory.advice

import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.advanced.trace.logtrace.models.TraceStatus
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation

class LogTraceAdvice(
    private val trace: LogTrace
): MethodInterceptor {
    override fun invoke(invocation: MethodInvocation): Any? {
        var status: TraceStatus? = null

        try {
            val method = invocation.method
            val message = "${method.declaringClass.simpleName}.${method.name}()"

            status = trace.begin(message)

            // 로직 호출
            val result = invocation.proceed()

            trace.end(status)
            return result
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e
        }
    }
}
