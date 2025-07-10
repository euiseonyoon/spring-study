package com.example.demo.aop.order

import com.example.demo.common.logger
import org.springframework.stereotype.Repository

@Repository
class OrderRepository {

    private val log = logger()

    fun save(itemId: String): String {
        log.info("[orderRepository] 실행")

        // 저장 로직
        if (itemId == "ex") {
            throw IllegalStateException("예외 발생")
        }
        return "ok"
    }
}