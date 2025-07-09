package com.example.demo.advanced.app

import com.example.demo.advanced.trace.logtrace.AbstractTemplate
import com.example.demo.advanced.trace.logtrace.AbstractTemplate2
import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.advanced.trace.logtrace.models.TraceStatus
import lombok.RequiredArgsConstructor
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
        val template: AbstractTemplate<String> = object : AbstractTemplate<String>(trace) {
            override fun call(): String {
                orderService.orderItem(itemId)
                return "ok"
            }
        }

        val template2 = AbstractTemplate2<String>(trace) {
            orderService.orderItem(itemId)
            "ok"
        }
        val result2 = template2.execute("OrderController.request()")!!

        return template.execute("OrderController.request()")!!
    }
}
