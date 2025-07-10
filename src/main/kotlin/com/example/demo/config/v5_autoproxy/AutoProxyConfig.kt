package com.example.demo.config.v5_autoproxy

import com.example.demo.advanced.trace.logtrace.LogTrace
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
class AutoProxyConfig {

    // AnnotationAwareAspectJAutoProxyCreator가 이미 등록되어 있다 ("org.springframework.boot:spring-boot-starter-aop")
    // 어디에 적용할 지(pointcut)만 있으면 proxy에 적용할 수 있다.
    @Bean
    fun advisor1(trace: LogTrace) : Advisor {
        // pointcut
        val pointcut = NameMatchMethodPointcut()
        pointcut.setMappedNames("request*", "order*", "save*")
        // advice
        val advice = LogTraceAdvice(trace)

        return DefaultPointcutAdvisor(pointcut, advice)
    }

}