package com.example.demo.proxy.cglib.code

import org.springframework.cglib.proxy.MethodInterceptor
import org.springframework.cglib.proxy.MethodProxy
import java.lang.reflect.Method

class TimeMethodInterceptor(
    private val target: Any,
): MethodInterceptor {

    override fun intercept(
        obj: Any?,
        method: Method?,
        args: Array<Any?>?,
        methodProxy: MethodProxy?
    ): Any? {
        val startTime = System.currentTimeMillis()

        val result = methodProxy?.invoke(target, args)

        val endTime = System.currentTimeMillis()
        val execTime = endTime - startTime

        println("TimeProxy 종료. execTimeMs=${execTime}")

        return result
    }
}