package com.example.demo.advanced.app

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
        var status: TraceStatus? = null
        try{
            status = trace.begin("OrderService.orderItem()")
            orderRepository.save(itemId)
            trace.end(status)
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e
        }
    }
}