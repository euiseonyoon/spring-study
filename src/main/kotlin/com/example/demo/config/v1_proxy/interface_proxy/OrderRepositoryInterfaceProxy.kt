package com.example.demo.config.v1_proxy.interface_proxy

import com.example.demo.advanced.app.v1.OrderRepositoryV1
import com.example.demo.advanced.app.v1.OrderRepositoryV1Impl
import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.advanced.trace.logtrace.models.TraceStatus

class OrderRepositoryInterfaceProxy(
    private val trace: LogTrace,
    private val target: OrderRepositoryV1Impl
): OrderRepositoryV1 {
    override fun save(itemId: String) {
        var status: TraceStatus? = null
        try {
            status = trace.begin("OrderRepositoryV1.save()")

            target.save(itemId)

            trace.end(status)
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e
        }
    }
}
