package com.example.demo.config.v3_proxy_factory.advice

import com.example.demo.advanced.app.v1.OrderControllerV1
import com.example.demo.advanced.app.v1.OrderControllerV1Impl
import com.example.demo.advanced.app.v1.OrderRepositoryV1
import com.example.demo.advanced.app.v1.OrderRepositoryV1Impl
import com.example.demo.advanced.app.v1.OrderServiceV1
import com.example.demo.advanced.app.v1.OrderServiceV1Impl
import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.common.logger
import org.springframework.aop.Advisor
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.NameMatchMethodPointcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProxyFactoryConfigV1(
    private val trace: LogTrace
) {
    private val log = logger()

    private fun getAdvisor(trace: LogTrace) : Advisor {
        val pointcut = NameMatchMethodPointcut()
        pointcut.setMappedNames("request*", "order*", "save*")
        val advisor = LogTraceAdvice(trace)

        return DefaultPointcutAdvisor(pointcut, advisor)
    }

    @Bean
    fun orderRepositoryV1(trace: LogTrace): OrderRepositoryV1 {
        val orderRepositoryV1 = OrderRepositoryV1Impl()
        val proxyFactory = ProxyFactory(orderRepositoryV1)
        proxyFactory.addAdvisor(getAdvisor(trace))
        val proxy = proxyFactory.proxy as OrderRepositoryV1

        log.info("ProxyFactory proxy={${proxy.javaClass}}, target={${orderRepositoryV1.javaClass}}")
        return proxy
    }

    @Bean
    fun orderServiceV1(trace: LogTrace): OrderServiceV1 {
        val orderServiceV1 = OrderServiceV1Impl(orderRepositoryV1(trace))
        val proxyFactory = ProxyFactory(orderServiceV1)
        proxyFactory.addAdvisor(getAdvisor(trace))
        val proxy = proxyFactory.proxy as OrderServiceV1

        log.info("ProxyFactory proxy={${proxy.javaClass}}, target={${orderServiceV1.javaClass}}")
        return proxy
    }

    @Bean
    fun orderControllerV1(trace: LogTrace): OrderControllerV1 {
        val orderControllerV1 = OrderControllerV1Impl(orderServiceV1(trace))
        val proxyFactory = ProxyFactory(orderControllerV1)
        proxyFactory.addAdvisor(getAdvisor(trace))
        val proxy = proxyFactory.proxy as OrderControllerV1

        log.info("ProxyFactory proxy={${proxy.javaClass}}, target={${orderControllerV1.javaClass}}")
        return proxy
    }
}