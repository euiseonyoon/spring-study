package com.example.demo.advanced.trace.logtrace

import com.example.demo.advanced.trace.logtrace.models.TraceStatus

// Template Callback Method using Strategy pattern
class AbstractTemplate2 <T> (
    private val trace: LogTrace,
    private val operation: () -> T?
) {
    fun execute(message: String): T? {
        var status: TraceStatus? = null
        try {
            status = trace.begin(message)

            val result = operation()

            trace.end(status)
            return result
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e
        }
    }

}