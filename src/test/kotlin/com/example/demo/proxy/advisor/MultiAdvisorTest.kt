package com.example.demo.proxy.advisor

import com.example.demo.proxy.common.service.ServiceImpl
import com.example.demo.proxy.common.service.ServiceInterface
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.aop.Pointcut
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.DefaultPointcutAdvisor

class MultiAdvisorTest {

    @Test
    @DisplayName("(프록시1개 with 1개 advisor) * N개")
    fun multiAdvisorTest1() {
        // client -> Proxy2(advisor2) -> Proxy1(advisor1) --> target

        // 프록시1 생성
        val target: ServiceInterface = ServiceImpl()
        val proxyFactory1 = ProxyFactory(target)
        val advisor1 = DefaultPointcutAdvisor(Pointcut.TRUE, Advice1())
        proxyFactory1.addAdvisor(advisor1)
        val proxy1 = proxyFactory1.proxy as ServiceInterface

        // 프록시2 생성
        val proxyFactory2 = ProxyFactory(proxy1)
        val advisor2 = DefaultPointcutAdvisor(Pointcut.TRUE, Advice2())
        proxyFactory2.addAdvisor(advisor2)
        val proxy2 = proxyFactory2.proxy as ServiceInterface

        proxy2.save()
        // 문졔: Proxy를 advisor 개수만큼 생성해야됨
    }

    @Test
    @DisplayName("하나의 프록시 with 여러 advisor")
    fun multiAdvisorTest2() {
        // client -> Proxy -> advisor2 -> advisor1 --> target

        val advisor1 = DefaultPointcutAdvisor(Pointcut.TRUE, Advice1())
        val advisor2 = DefaultPointcutAdvisor(Pointcut.TRUE, Advice2())

        val target: ServiceInterface = ServiceImpl()
        val proxyFactory1 = ProxyFactory(target)

        // add 하는 순서 중요
        proxyFactory1.addAdvisor(advisor2)
        proxyFactory1.addAdvisor(advisor1)

        val proxy = proxyFactory1.proxy as ServiceInterface

        proxy.save()
        /**
         * Spring은 AOP를 적용할 떄, target 마다 하나의 프록시만을 생성한다.
         * 그리고 여러개의 advisor를 적용하여 최적화 한다.
         * */
    }
}
