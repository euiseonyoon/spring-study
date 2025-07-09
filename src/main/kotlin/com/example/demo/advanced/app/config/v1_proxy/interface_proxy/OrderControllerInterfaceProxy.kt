package com.example.demo.advanced.app.config.v1_proxy.interface_proxy

import com.example.demo.advanced.app.v1.OrderControllerV1
import com.example.demo.advanced.app.v1.OrderControllerV1Impl
import com.example.demo.advanced.app.v1.OrderRepositoryV1
import com.example.demo.advanced.app.v1.OrderRepositoryV1Impl
import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.advanced.trace.logtrace.models.TraceStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

class OrderControllerInterfaceProxy(
    private val trace: LogTrace,
    private val target: OrderControllerV1Impl
): OrderControllerV1 {
    override fun request(itemId: String): String {
        var status: TraceStatus? = null
        try {
            status = trace.begin("OrderControllerV1.request()")

            target.request(itemId)

            trace.end(status)
            return "ok"
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e
        }
    }

    override fun noLog(): String? {
        return null
    }
}
