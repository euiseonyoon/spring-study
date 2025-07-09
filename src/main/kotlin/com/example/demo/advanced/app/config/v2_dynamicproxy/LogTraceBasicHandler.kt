package com.example.demo.advanced.app.config.v2_dynamicproxy

import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.advanced.trace.logtrace.models.TraceStatus
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class LogTraceBasicHandler(
    private val trace: LogTrace,
    private val target: Any,
): InvocationHandler {

    override fun invoke(
        proxy: Any?,
        method: Method,
        args: Array<Any>?
    ): Any? {
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