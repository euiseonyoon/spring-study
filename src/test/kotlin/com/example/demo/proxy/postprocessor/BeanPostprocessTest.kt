package com.example.demo.proxy.postprocessor

import com.example.demo.common.logger
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class BeanPostprocessTest {

    private val log = logger()

    @Test
    fun beanPostProcessorConfig() {
        val applicationContext: ApplicationContext = AnnotationConfigApplicationContext(BeanPostProcessorConfig::class.java)

        /**
         *         // A는 빈으로 등록
         *         Bean named 'beanA' is expected to be of type 'com.example.demo.proxy.postprocessor.BeanPostprocessTest$A'
         *         but was actually of type 'com.example.demo.proxy.postprocessor.BeanPostprocessTest$B'
         *         라는 에러가 나옴
         *
         *         val a = applicationContext.getBean("beanA", A::class.java). <-  여기서
         *         a.helloA()
         *
         *         // B는 빈으로 등록이 안됨
         *         assertThrows<NoSuchBeanDefinitionException> {
         *             applicationContext.getBean(B::class.java)
         *         }
         *
         */

        val b = applicationContext.getBean("beanA", B::class.java)
        b.helloB()

        assertThrows<NoSuchBeanDefinitionException> {
            applicationContext.getBean(A::class.java)
        }
    }

    class AtoBPostProcessor : BeanPostProcessor {
        private val log = logger()

        override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
            log.info("beanName={$beanName}, bean={$bean}")

            // 바꿔치기
            if (bean is A) {
                return B()
            }

            return bean
        }
    }

    @Configuration
    class BeanPostProcessorConfig {

        @Bean(name = ["beanA"])
        fun a(): A = A()

        @Bean
        fun atoBPostProcessor(): AtoBPostProcessor {
            return AtoBPostProcessor()
        }
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