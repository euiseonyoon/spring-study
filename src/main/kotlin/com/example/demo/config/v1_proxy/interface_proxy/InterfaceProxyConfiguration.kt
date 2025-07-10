package com.example.demo.config.v1_proxy.interface_proxy

import org.springframework.context.annotation.Configuration

@Configuration
class InterfaceProxyConfiguration {

//    @Bean
//    fun orderRepositoryV1(
//        trace: LogTrace
//    ): OrderRepositoryV1 {
//        return OrderRepositoryInterfaceProxy(trace, OrderRepositoryV1Impl())
//    }
//
//    @Bean
//    fun orderServiceV1(
//        trace: LogTrace,
//        orderRepositoryV1: OrderRepositoryV1
//    ) : OrderServiceV1 {
//        return OrderServiceInterfaceProxy(trace, OrderServiceV1Impl(orderRepositoryV1))
//    }
//
//    @Bean
//    fun orderControllerV1(
//        trace: LogTrace,
//        orderServiceV1: OrderServiceV1
//    ): OrderControllerV1 {
//        return OrderControllerInterfaceProxy(trace, OrderControllerV1Impl(orderServiceV1))
//    }
}