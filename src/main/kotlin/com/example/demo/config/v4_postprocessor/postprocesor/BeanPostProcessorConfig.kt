package com.example.demo.config.v4_postprocessor.postprocesor

import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.common.logger
import com.example.demo.config.AppV1Config
import com.example.demo.config.AppV2Config
import com.example.demo.config.v3_proxy_factory.advice.LogTraceAdvice
import org.springframework.aop.Advisor
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.NameMatchMethodPointcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(AppV1Config::class, AppV2Config::class)
class BeanPostProcessorConfig {

    private val log = logger()

    @Bean
    fun logTracePostProcessor(
        trace: LogTrace
    ) : PackageLogTracePostProcessor {
        // 해당 path 아래의 bean에 proxy를 적용하겠다
        val basePackage = "com.example.demo.advanced.app"
        return PackageLogTracePostProcessor(basePackage, getAdvisor(trace))
    }

    private fun getAdvisor(trace: LogTrace) : Advisor {
        // pointcut
        val pointcut = NameMatchMethodPointcut()
        pointcut.setMappedNames("request*", "order*", "save*")
        // advice
        val advice = LogTraceAdvice(trace)

        return DefaultPointcutAdvisor(pointcut, advice)
    }
}