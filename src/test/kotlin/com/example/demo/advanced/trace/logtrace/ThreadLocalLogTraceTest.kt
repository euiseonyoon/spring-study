package com.example.demo.advanced.trace.logtrace

import com.example.demo.advanced.trace.logtrace.models.TraceStatus
import com.example.demo.common.logger

class ThreadLocalLogTraceTest {

    val trace = ThreadLocalLogTrace()

    @org.junit.jupiter.api.Test
    fun begin_end_level2() {
        val status1: TraceStatus = trace.begin("hello1")
        val status2: TraceStatus = trace.begin("hello2")
        trace.end(status2)
        trace.end(status1)
    }

    @org.junit.jupiter.api.Test
    fun begin_exception_level2() {
        val status1: TraceStatus = trace.begin("hello1")
        val status2: TraceStatus = trace.begin("hello2")
        trace.exception(status2, IllegalStateException())
        trace.exception(status1, IllegalStateException())
    }
}