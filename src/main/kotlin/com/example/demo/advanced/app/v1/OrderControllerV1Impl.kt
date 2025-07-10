package com.example.demo.advanced.app.v1

import com.example.demo.config.dynmic_proxy.OpenClass

@OpenClass
class OrderControllerV1Impl(
    private val orderService: OrderServiceV1
) : OrderControllerV1 {
    override fun request(itemId: String): String {
        orderService.orderItem(itemId)
        return "ok"
    }

    override fun noLog(): String? {
        return "ok"
    }
}
