package com.example.demo.aop.order

import com.example.demo.common.logger
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {
    private val log = logger()

    fun orderItem(itemId: String) {
        log.info("[orderService] 실행")
        orderRepository.save(itemId)
    }
}
