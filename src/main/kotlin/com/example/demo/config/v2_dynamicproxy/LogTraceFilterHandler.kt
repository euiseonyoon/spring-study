package com.example.demo.config.v2_dynamicproxy

import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.advanced.trace.logtrace.models.TraceStatus
import org.springframework.util.PatternMatchUtils
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class LogTraceFilterHandler(
    private val trace: LogTrace,
    private val target: Any,
    private val patterns: Array<String>,
): InvocationHandler {

    override fun invoke(
        proxy: Any?,
        method: Method,
        args: Array<Any>?
    ): Any? {

        // 메서드 이름 필터
        // save, request, req*, *est
        if( !PatternMatchUtils.simpleMatch(patterns, method.name)) {
            return method.invoke(target, *(args ?: emptyArray()))
        }

        val message = "${method.declaringClass.simpleName}.${method.name}()"
        var status: TraceStatus? = null
        try {
            status = trace.begin(message)

            // 로직 호출
            val result = method.invoke(target, *(args ?: emptyArray()))

            trace.end(status)
            return result
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e
        }
    }
}