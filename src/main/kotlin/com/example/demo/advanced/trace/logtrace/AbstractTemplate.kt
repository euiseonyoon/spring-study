package com.example.demo.advanced.trace.logtrace

import com.example.demo.advanced.trace.logtrace.models.TraceStatus

// Template Method
abstract class AbstractTemplate <T> (
    private val trace: LogTrace,
) {

    protected abstract fun call(): T?

    fun execute(message: String): T? {
        var status: TraceStatus? = null
        try {
            status = trace.begin(message)

            val result = call()

            trace.end(status)
            return result
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e
        }
    }

}