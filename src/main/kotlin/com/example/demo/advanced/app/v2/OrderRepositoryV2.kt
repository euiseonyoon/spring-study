package com.example.demo.advanced.app.v2

import com.example.demo.config.dynmic_proxy.OpenClass

@OpenClass
class OrderRepositoryV2 {
    fun save(itemId: String) {
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
