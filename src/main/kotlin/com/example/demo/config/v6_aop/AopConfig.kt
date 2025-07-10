package com.example.demo.config.v6_aop

import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.config.AppV1Config
import com.example.demo.config.AppV2Config
import com.example.demo.config.v6_aop.aspect.LogTraceAspect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(AppV1Config::class, AppV2Config::class)
class AopConfig {

    @Bean
    fun logTraceAspect(
        trace: LogTrace
    ) : LogTraceAspect {
        return LogTraceAspect(trace)
    }
}
