package com.example.demo.config.v5_autoproxy

import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.config.AppV1Config
import com.example.demo.config.AppV2Config
import com.example.demo.config.v3_proxy_factory.advice.LogTraceAdvice
import org.springframework.aop.Advisor
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.NameMatchMethodPointcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

//@Configuration
//@Import(AppV1Config::class, AppV2Config::class)
//class AutoProxyConfig {
//
//    // AnnotationAwareAspectJAutoProxyCreator가 이미 등록되어 있다 ("org.springframework.boot:spring-boot-starter-aop")
//    // 어디에 적용할 지(pointcut)만 있으면 proxy에 적용할 수 있다.
//    // @Bean
//    fun advisor1(trace: LogTrace) : Advisor {
//        // 문제: 이렇게 되면 다른 어떠한 bean에 request, order, save  라는 메소드가 있으면
//        // 그 bean에 모두 프록시 처리가 된다.
//        // 따라서 단순 이름만이 아닌 더욱 정밀한 pointcut이 필요하다.
//         val pointcut = NameMatchMethodPointcut()
//         pointcut.setMappedNames("request*", "order*", "save*")
//
//        // advice
//        val advice = LogTraceAdvice(trace)
//
//        return DefaultPointcutAdvisor(pointcut, advice)
//    }
//
//    // @Bean
//    fun advisor2(trace: LogTrace) : Advisor {
//        // pointcut
//        val pointcut = AspectJExpressionPointcut()
//        // 이렇게 하면 com.example.demo.advanced.app 하위의 모든 bean의 모든 메서드에 advice가 적용됨 -> 문제: noLog 메서드에도 ..
//        pointcut.expression = ("execution(* com.example.demo.advanced.app..*(..))")
//
//        // advice
//        val advice = LogTraceAdvice(trace)
//
//        return DefaultPointcutAdvisor(pointcut, advice)
//    }
//
//    @Bean
//    fun advisor3(trace: LogTrace) : Advisor {
//        // pointcut
//        val pointcut = AspectJExpressionPointcut()
//        // noLog라는 메서드에는 advice를 적용하지 않는다.
//        pointcut.expression = ("execution(* com.example.demo.advanced.app..*(..)) && !execution(* com.example.demo.advanced.app..noLog(..))")
//
//        // advice
//        val advice = LogTraceAdvice(trace)
//
//        return DefaultPointcutAdvisor(pointcut, advice)
//    }
//}
