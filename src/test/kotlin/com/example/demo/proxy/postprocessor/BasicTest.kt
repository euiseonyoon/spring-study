package com.example.demo.proxy.postprocessor

import com.example.demo.common.logger
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class BasicTest {

    private val log = logger()

    @Test
    fun basicConfig() {
        val applicationContext: ApplicationContext = AnnotationConfigApplicationContext(BasicConfig::class.java)

        // A는 빈으로 등록
        val a = applicationContext.getBean("beanA", A::class.java)
        a.helloA()

        // B는 빈으로 등록이 안됨
        assertThrows<NoSuchBeanDefinitionException> {
            applicationContext.getBean(B::class.java)
        }
    }

    @Configuration
    class BasicConfig {
        @Bean(name = ["beanA"])
        fun a(): A = A()
    }

    class A {
        private val log = logger()

        fun helloA() {
            log.info("hello A")
        }
    }

    class B {
        private val log = logger()

        fun helloB() {
            log.info("hello B")
        }
    }
}