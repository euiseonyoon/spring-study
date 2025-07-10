package com.example.demo.advanced.app.v2

import com.example.demo.config.dynmic_proxy.OpenClass

@OpenClass
class OrderServiceV2(private val orderRepository: OrderRepositoryV2) {
    fun orderItem(itemId: String) {
        orderRepository.save(itemId)
    }
}
