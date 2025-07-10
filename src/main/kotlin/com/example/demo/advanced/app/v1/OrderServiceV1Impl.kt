package com.example.demo.advanced.app.v1

import com.example.demo.config.dynmic_proxy.OpenClass

@OpenClass
class OrderServiceV1Impl(
    private val orderRepository: OrderRepositoryV1
) : OrderServiceV1 {
    override fun orderItem(itemId: String) {
        orderRepository.save(itemId)
    }
}
