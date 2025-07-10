package com.example.demo.proxy.cglib

import com.example.demo.proxy.cglib.code.TimeMethodInterceptor
import com.example.demo.proxy.common.service.ConcreteService
import org.junit.jupiter.api.Test
import org.springframework.cglib.proxy.Enhancer

class CglibTest {
    @Test
    fun cglib() {
        val target = ConcreteService()
        val enhancer = Enhancer()
        enhancer.setSuperclass(ConcreteService::class.java)
        enhancer.setCallback(TimeMethodInterceptor(target))

        // cglib proxy
        val proxy = enhancer.create().let {
            it as ConcreteService
        }

        // targetClass={class com.example.demo.config.dynmic_proxy.ConcreteService}
        println("targetClass={${target.javaClass}}")
        // proxyClass={class com.example.demo.config.dynmic_proxy.ConcreteService$$EnhancerByCGLIB$$18be73d8}
        println("proxyClass={${proxy.javaClass}}")

        val stop =1
    }
}