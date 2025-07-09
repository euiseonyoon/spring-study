package com.example.demo.advanced.app

import com.example.demo.advanced.trace.logtrace.AbstractTemplate
import com.example.demo.advanced.trace.logtrace.AbstractTemplate2
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
        val template: AbstractTemplate<Unit> = object : AbstractTemplate<Unit>(trace) {
            override fun call() {
                if (itemId == "ex") {
                    throw IllegalStateException("예외!!!")
                }
                sleep(1000)
            }
        }

        val template2 = AbstractTemplate2(trace) {
            if (itemId == "ex") {
                throw IllegalStateException("예외!!!")
            }
            sleep(1000)
        }
        val result = template2.execute("OrderRepository.save()")
        template.execute("OrderRepository.save()")
    }
}
