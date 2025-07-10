package com.example.demo.proxy.advisor

import com.example.demo.common.logger
import org.springframework.aop.ClassFilter

class MyClassFilter: ClassFilter {
    private val log = logger()
    private val ALLOWED_INTERFACE_NAME = "ServiceInterface"

    override fun matches(clazz: Class<*>): Boolean {
        // ServiceInterface 라는 interface를 구현한 구현체에만 advice를 적용해보자

        var matched = false
        var filteredReason = "$ALLOWED_INTERFACE_NAME 인터페이스의 구현체에만 적용됩니다."

        val interfaces = clazz.interfaces
        if (interfaces.isEmpty()) {
            filteredReason = "Interface가 없습니다."
        }

        interfaces.forEach { it ->
            if (it.simpleName.contains(ALLOWED_INTERFACE_NAME)) {
                matched = true
            }
        }
        log.info("MyClassFilter. matched={$matched}, filteredReason={$filteredReason}")
        return matched
    }
}