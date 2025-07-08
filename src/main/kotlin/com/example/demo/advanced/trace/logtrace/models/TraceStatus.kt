package com.example.demo.advanced.trace.logtrace.models

data class TraceStatus(
    val traceId: TraceId,
    val startTimeMs: Long,
    val message: String
)
