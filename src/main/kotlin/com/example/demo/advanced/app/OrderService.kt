package com.example.demo.advanced.app

import com.example.demo.advanced.trace.logtrace.AbstractTemplate
import com.example.demo.advanced.trace.logtrace.AbstractTemplate2
import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.advanced.trace.logtrace.ThreadLocalLogTrace
import com.example.demo.advanced.trace.logtrace.models.TraceStatus
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val trace: LogTrace
) {

    fun orderItem(itemId: String) {
        val template: AbstractTemplate<Unit> = object : AbstractTemplate<Unit>(trace) {
            override fun call() {
                orderRepository.save(itemId)
            }
        }

        val template2 = AbstractTemplate2(trace) { orderRepository.save(itemId) }
        val result2 = template2.execute("OrderService.orderItem()")

        template.execute("OrderService.orderItem()")
    }
}