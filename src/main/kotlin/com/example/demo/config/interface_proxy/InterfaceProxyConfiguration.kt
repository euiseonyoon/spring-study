package com.example.demo.config.interface_proxy

import com.example.demo.advanced.app.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy
import com.example.demo.advanced.app.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy
import com.example.demo.advanced.app.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy
import com.example.demo.advanced.app.v1.OrderControllerV1
import com.example.demo.advanced.app.v1.OrderControllerV1Impl
import com.example.demo.advanced.app.v1.OrderRepositoryV1
import com.example.demo.advanced.app.v1.OrderRepositoryV1Impl
import com.example.demo.advanced.app.v1.OrderServiceV1
import com.example.demo.advanced.app.v1.OrderServiceV1Impl
import com.example.demo.advanced.trace.logtrace.LogTrace
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InterfaceProxyConfiguration {

    @Bean
    fun orderRepositoryV1(
        trace: LogTrace
    ): OrderRepositoryV1 {
        return OrderRepositoryInterfaceProxy(trace, OrderRepositoryV1Impl())
    }

    @Bean
    fun orderServiceV1(
        trace: LogTrace,
        orderRepositoryV1: OrderRepositoryV1
    ) : OrderServiceV1 {
        return OrderServiceInterfaceProxy(trace, OrderServiceV1Impl(orderRepositoryV1))
    }

    @Bean
    fun orderControllerV1(
        trace: LogTrace,
        orderServiceV1: OrderServiceV1
    ): OrderControllerV1 {
        return OrderControllerInterfaceProxy(trace, OrderControllerV1Impl(orderServiceV1))
    }
}