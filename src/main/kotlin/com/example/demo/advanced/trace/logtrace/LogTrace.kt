package com.example.demo.advanced.trace.logtrace

import com.example.demo.advanced.trace.logtrace.models.TraceStatus

interface LogTrace {
    fun begin(message: String): TraceStatus
    fun end(status: TraceStatus)
    fun exception(status: TraceStatus?, e: Exception)
}