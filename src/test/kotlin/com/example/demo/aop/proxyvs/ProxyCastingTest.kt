package com.example.demo.aop.proxyvs

import com.example.demo.aop.order.aop.member.MemberService
import com.example.demo.aop.order.aop.member.MemberServiceImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.aop.framework.ProxyFactory


class ProxyCastingTest {

    @Test
    fun jdkProxy() {
        val target = MemberServiceImpl()
        val proxyFactory = ProxyFactory(target)
        proxyFactory.isProxyTargetClass = false

        val proxy = proxyFactory.proxy

        assertDoesNotThrow { proxy as MemberService }
        assertThrows<ClassCastException> { proxy as MemberServiceImpl } // 이렇게 JDK 동적 프록시를 구현체로 casting 하려면 에러남
        // 왜냐, jdk 동적 프록시는 프록시 생성시 target이 interface를 가지고 있어야 하고, 그 interface를 이용하니까
    }

    @Test
    fun cglibProxy() {
        val target = MemberServiceImpl()
        val proxyFactory = ProxyFactory(target)
        proxyFactory.isProxyTargetClass = true

        val proxy = proxyFactory.proxy

        assertDoesNotThrow { proxy as MemberService }
        assertDoesNotThrow { proxy as MemberServiceImpl } // CGLIB 프록시는 interface, 구현 객체 둘 다 casting 됨
        // 왜냐, CGLIB 프록시는 프록시 생성시 target의 구현체 타입(concrete 클래스타입)을 기반으로 프록시를 생성하니까
    }
    /**
     * 이 casting 문제는 의존관계 주입시 문제가 될 수 있다.
     * */
}
