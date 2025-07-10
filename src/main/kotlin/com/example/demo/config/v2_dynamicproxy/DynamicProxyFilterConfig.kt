package com.example.demo.config.v2_dynamicproxy

import com.example.demo.advanced.app.v1.OrderControllerV1
import com.example.demo.advanced.app.v1.OrderControllerV1Impl
import com.example.demo.advanced.app.v1.OrderRepositoryV1
import com.example.demo.advanced.app.v1.OrderRepositoryV1Impl
import com.example.demo.advanced.app.v1.OrderServiceV1
import com.example.demo.advanced.app.v1.OrderServiceV1Impl
import com.example.demo.advanced.trace.logtrace.LogTrace
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Proxy

@Configuration
class DynamicProxyFilterConfig {

//    val ALLOWED_PATTERNS = arrayOf("save*", "order*", "request*")
//
//    @Bean
//    fun orderControllerV1(
//        logTrace: LogTrace,
//        orderServiceV1: OrderServiceV1,
//    ): OrderControllerV1 {
//        val controller = OrderControllerV1Impl(orderServiceV1)
//        val proxy = Proxy.newProxyInstance(
//            OrderControllerV1::class.java.classLoader,
//            arrayOf(OrderControllerV1::class.java),
//            LogTraceFilterHandler(logTrace, controller, ALLOWED_PATTERNS)
//        ) as OrderControllerV1
//        return proxy
//    }
//
//    @Bean
//    fun orderServiceV1(
//        logTrace: LogTrace,
//        orderRepositoryV1: OrderRepositoryV1,
//    ): OrderServiceV1 {
//        val service: OrderServiceV1 = OrderServiceV1Impl(orderRepositoryV1)
//        val proxy = Proxy.newProxyInstance(
//            OrderServiceV1::class.java.classLoader,
//            arrayOf(OrderServiceV1::class.java),
//            LogTraceFilterHandler(logTrace, service, ALLOWED_PATTERNS)
//        ) as OrderServiceV1
//        return proxy
//    }
//
//    @Bean
//    fun orderRepositoryV1(
//        logTrace: LogTrace
//    ) : OrderRepositoryV1 {
//
//        val orderRepo: OrderRepositoryV1 = OrderRepositoryV1Impl()
//        val proxy = Proxy.newProxyInstance(
//            OrderRepositoryV1::class.java.classLoader,
//            arrayOf(OrderRepositoryV1::class.java),
//            LogTraceFilterHandler(logTrace, orderRepo, ALLOWED_PATTERNS)
//        ) as OrderRepositoryV1
//        return proxy
//    }
}