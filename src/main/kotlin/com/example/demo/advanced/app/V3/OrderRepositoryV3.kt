package com.example.demo.advanced.app.V3

import org.springframework.stereotype.Repository

@Repository
open class OrderRepositoryV3 {
    open fun save(itemId: String) {
        //저장 로직
        check(itemId != "ex") { "예외 발생!" }
        sleep(1000)
    }

    private fun sleep(millis: Int) {
        try {
            Thread.sleep(millis.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
