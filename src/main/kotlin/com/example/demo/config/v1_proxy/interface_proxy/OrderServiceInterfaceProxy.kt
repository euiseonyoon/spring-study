package com.example.demo.config.v1_proxy.interface_proxy

import com.example.demo.advanced.app.v1.OrderRepositoryV1
import com.example.demo.advanced.app.v1.OrderRepositoryV1Impl
import com.example.demo.advanced.app.v1.OrderServiceV1
import com.example.demo.advanced.app.v1.OrderServiceV1Impl
import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.advanced.trace.logtrace.models.TraceStatus

class OrderServiceInterfaceProxy(
    private val trace: LogTrace,
    private val target: OrderServiceV1Impl
): OrderServiceV1 {
    override fun orderItem(itemId: String) {
        var status: TraceStatus? = null
        try {
            status = trace.begin("OrderServiceV1.orderItem()")

            target.orderItem(itemId)

            trace.end(status)
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e
        }
    }
}
