package com.example.demo.advanced.app.V3

import org.springframework.stereotype.Service

@Service
class OrderServiceV3(
    private val orderRepository: OrderRepositoryV3
) {
    fun orderItem(itemId: String) {
        orderRepository.save(itemId)
    }
}
