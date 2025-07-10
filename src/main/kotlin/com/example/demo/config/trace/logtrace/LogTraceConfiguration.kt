package com.example.demo.config.trace.logtrace

import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.advanced.trace.logtrace.ThreadLocalLogTrace
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LogTraceConfiguration {
    @Bean
    fun logTrace() : LogTrace = ThreadLocalLogTrace()
}