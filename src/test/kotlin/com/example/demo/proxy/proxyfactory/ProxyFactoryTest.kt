package com.example.demo.proxy.proxyfactory

import com.example.demo.common.logger
import com.example.demo.proxy.common.advice.TimeAdvice
import com.example.demo.proxy.common.service.ConcreteService
import com.example.demo.proxy.common.service.ServiceImpl
import com.example.demo.proxy.common.service.ServiceInterface
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.AopUtils
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProxyFactoryTest {

    private val log = logger()

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시를 사용합니다.")
    fun jdkDynamicProxy() {

        val target: ServiceInterface = ServiceImpl()
        val proxyFactory = ProxyFactory(target)
        val advice = TimeAdvice()
        proxyFactory.addAdvice(advice)
        val proxy = proxyFactory.proxy as ServiceInterface

        // targetClass={class com.example.demo.proxy.common.service.ServiceImpl}
        log.info("targetClass={${target.javaClass}}")
        // proxyClass={class jdk.proxy3.$Proxy17}
        log.info("proxyClass={${proxy.javaClass}}")

        proxy.save()

        // ProxyFactory를 통해 proxy를 만들었을때만 아래 AopUtils 메소드들의 사용이 가능하다.
        assertTrue { AopUtils.isAopProxy(proxy) }
        if ( !proxyFactory.isProxyTargetClass ) {
            assertTrue { AopUtils.isJdkDynamicProxy(proxy) }
            assertFalse { AopUtils.isCglibProxy(proxy) }
        }
    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB를 사용합니다.")
    fun cglibProxy() {

        val target = ConcreteService()
        val proxyFactory = ProxyFactory(target)
        val advice = TimeAdvice()
        proxyFactory.addAdvice(advice)
        val proxy = proxyFactory.proxy as ConcreteService

        // targetClass={class com.example.demo.proxy.common.service.ConcreteService}
        log.info("targetClass={${target.javaClass}}")
        // proxyClass={class com.example.demo.proxy.common.service.ConcreteService$$SpringCGLIB$$0}
        log.info("proxyClass={${proxy.javaClass}}")

        proxy.call()

        // ProxyFactory를 통해 proxy를 만들었을때만 아래 AopUtils 메소드들의 사용이 가능하다.
        assertTrue { AopUtils.isAopProxy(proxy) }
        assertFalse { AopUtils.isJdkDynamicProxy(proxy) }
        assertTrue { AopUtils.isCglibProxy(proxy) }
    }

    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 interface가 있어도, 클래스기반(CGLIB) 프록시를 생성합니다.")
    fun proxyTargetClass() {
        // NOTE: 주의!! 반듯이 return type을 interface로 하도록 하자
        val target: ServiceInterface = ServiceImpl()
        val proxyFactory = ProxyFactory(target)

        // interface가 있어도. 클래스기반 프록시를 만들게 강제한다.
        proxyFactory.isProxyTargetClass = true

        val advice = TimeAdvice()
        proxyFactory.addAdvice(advice)

        // NOTE: 주의!! 여기서도 interface로 casting 하도록 하자
        val proxy = proxyFactory.proxy as ServiceInterface

        // targetClass={class com.example.demo.proxy.common.service.ServiceImpl}
        log.info("targetClass={${target.javaClass}}")
        // proxyClass={class com.example.demo.proxy.common.service.ServiceImpl$$SpringCGLIB$$0}
        log.info("proxyClass={${proxy.javaClass}}")

        proxy.save()

        // ProxyFactory를 통해 proxy를 만들었을때만 아래 AopUtils 메소드들의 사용이 가능하다.
        assertTrue { AopUtils.isAopProxy(proxy) }
        assertTrue { proxyFactory.isProxyTargetClass }
        assertFalse { AopUtils.isJdkDynamicProxy(proxy) }
        assertTrue { AopUtils.isCglibProxy(proxy) }
    }
}
