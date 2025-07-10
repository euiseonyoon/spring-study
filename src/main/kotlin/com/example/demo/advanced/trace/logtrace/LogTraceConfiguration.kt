package com.example.demo.advanced.trace.logtrace

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LogTraceConfiguration {
    @Bean
    fun logTrace() : LogTrace = ThreadLocalLogTrace()
}