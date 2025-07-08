package com.example.demo.advanced.app

import com.example.demo.advanced.trace.logtrace.LogTrace
import com.example.demo.advanced.trace.logtrace.models.TraceStatus
import org.springframework.stereotype.Repository
import java.lang.Exception
import java.lang.Thread.sleep

@Repository
class OrderRepository(
    private val trace: LogTrace
) {
    fun save(itemId: String) {
        var status: TraceStatus? = null
        try {
            status = trace.begin("OrderRepository.save()")

            // 저장
            if (itemId == "ex") {
                throw IllegalStateException("예외!!!")
            }
            sleep(1000)
            trace.end(status)
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e
        }
    }

}