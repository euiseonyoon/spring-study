package com.example.demo.aop.order.aop.exam

import com.example.demo.aop.order.aop.exam.annotation.Retry
import com.example.demo.aop.order.aop.exam.annotation.Trace
import org.springframework.stereotype.Repository

@Repository
class ExamRepository {
    private var seq = 0

    // 5번에 한번 실패하는 요청
    @Trace
    @Retry
    fun save(itemId: String): String {
        seq++
        if (seq % 5 == 0) {
            throw IllegalStateException("예외 발생")
        }
        return "ok"
    }
}