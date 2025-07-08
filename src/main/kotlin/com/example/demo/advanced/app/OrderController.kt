package com.example.demo.advanced.app

import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.advanced.trace.logtrace.models.TraceStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
    private val orderService: OrderService,
    private val trace: LogTrace
) {
    @GetMapping("/request")
    fun request(
        @RequestParam itemId: String,
    ): String {
        var status: TraceStatus? = null
        try {
            status = trace.begin("OrderController.request()")
            orderService.orderItem(itemId)
            trace.end(status)
            return "ok"
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e
        }
    }
}