package com.example.demo.aop.order.aop.exam.aspect

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AopExamConfig {

    @Bean
    fun traceAspect(): TraceAspect = TraceAspect()

    @Bean
    fun retryAspect(): RetryAspect = RetryAspect()
}
