package com.example.demo.config.v3_proxy_factory.advice

import com.example.demo.advanced.app.v1.OrderControllerV1
import com.example.demo.advanced.app.v2.OrderControllerV2
import com.example.demo.advanced.app.v2.OrderRepositoryV2
import com.example.demo.advanced.app.v2.OrderServiceV2
import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.common.logger
import org.springframework.aop.Advisor
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.NameMatchMethodPointcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProxyFactoryConfigV2 {
//    private val log = logger()
//
//    private fun getAdvisor(trace: LogTrace) : Advisor {
//        val pointcut = NameMatchMethodPointcut()
//        pointcut.setMappedNames("request*", "order*", "save*")
//        val advice = LogTraceAdvice(trace)
//
//        return DefaultPointcutAdvisor(pointcut, advice)
//    }
//
//    @Bean
//    fun orderRepositoryV2(trace: LogTrace): OrderRepositoryV2 {
//        val orderRepositoryV2 = OrderRepositoryV2()
//        val proxyFactory = ProxyFactory(orderRepositoryV2)
//        proxyFactory.addAdvisor(getAdvisor(trace))
//        val proxy = proxyFactory.proxy as OrderRepositoryV2
//
//        log.info("ProxyFactory proxy={${proxy.javaClass}}, target={${orderRepositoryV2.javaClass}}")
//        return proxy
//    }
//
//    @Bean
//    fun orderServiceV2(trace: LogTrace): OrderServiceV2 {
//        val orderServiceV2 = OrderServiceV2(orderRepositoryV2(trace))
//        val proxyFactory = ProxyFactory(orderServiceV2)
//        proxyFactory.addAdvisor(getAdvisor(trace))
//        val proxy = proxyFactory.proxy as OrderServiceV2
//
//        log.info("ProxyFactory proxy={${proxy.javaClass}}, target={${orderServiceV2.javaClass}}")
//        return proxy
//    }
//
//    @Bean
//    fun orderControllerV2(trace: LogTrace): OrderControllerV2 {
//        val orderControllerV2 = OrderControllerV2(orderServiceV2(trace))
//        val proxyFactory = ProxyFactory(orderControllerV2)
//        proxyFactory.addAdvisor(getAdvisor(trace))
//        val proxy = proxyFactory.proxy as OrderControllerV2
//
//        log.info("ProxyFactory proxy={${proxy.javaClass}}, target={${orderControllerV2.javaClass}}")
//        return proxy
//    }
}