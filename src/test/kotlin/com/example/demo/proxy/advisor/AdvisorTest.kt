package com.example.demo.proxy.advisor

import com.example.demo.proxy.common.advice.TimeAdvice
import com.example.demo.proxy.common.service.ConcreteService
import com.example.demo.proxy.common.service.ServiceImpl
import com.example.demo.proxy.common.service.ServiceInterface
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.aop.Pointcut
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.NameMatchMethodPointcut
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor

class AdvisorTest {

    @Test
    fun advisorTest1() {
        val target: ServiceInterface = ServiceImpl()
        val proxyFactory = ProxyFactory(target)
        val advice = TimeAdvice()

        val advisor = DefaultPointcutAdvisor(Pointcut.TRUE, advice)
        /**
         * 여기서 Pointcut.TRUE의 의미:
         *   taget의 모든 메서드를 실행할때 부가기능(Advice)를 적용하겠다는 의미.
         *   하지만 현재의 TimeAdvice의 구조상 Pointcut.TRUE에 상관없이 모두 실행시간을 로깅하고 있다.
         *   직접 포인트컷을 만들어서 해결하도록 한다.
         * */

        proxyFactory.addAdvisor(advisor)
        // NOTE:
        // .addAdvice(advice)를 사용해도 결과적으로. DefaultPointcutAdvisor(Pointcut.TRUE, advice) 가 들어가게 된다
        // org.springframework.aop.framework.AdvisedSupport

        val proxy = proxyFactory.proxy as ServiceInterface

        proxy.save()
        proxy.find()

        /**
         * Log:
         *
         * 10:37:36.584 [Test worker] INFO com.example.demo.proxy.common.advice.TimeAdvice -- TimeProxy - TimeAdvice 실행
         * 10:37:36.585 [Test worker] INFO com.example.demo.proxy.common.service.ServiceImpl -- ServiceImpl save 호출
         * 10:37:36.586 [Test worker] INFO com.example.demo.proxy.common.advice.TimeAdvice -- execTime={0}
         * 10:37:36.586 [Test worker] INFO com.example.demo.proxy.common.advice.TimeAdvice -- TimeProxy - TimeAdvice 종료
         *
         * 10:37:36.586 [Test worker] INFO com.example.demo.proxy.common.advice.TimeAdvice -- TimeProxy - TimeAdvice 실행
         * 10:37:36.586 [Test worker] INFO com.example.demo.proxy.common.service.ServiceImpl -- ServiceImpl find 호출
         * 10:37:36.586 [Test worker] INFO com.example.demo.proxy.common.advice.TimeAdvice -- execTime={0}
         * 10:37:36.586 [Test worker] INFO com.example.demo.proxy.common.advice.TimeAdvice -- TimeProxy - TimeAdvice 종료
         *
         * */
    }


    @Test
    @DisplayName("직접 만드는 pointcut")
    fun advisorTest2() {
        val target: ServiceInterface = ServiceImpl()
        val proxyFactory = ProxyFactory(target)

        val advice = TimeAdvice()
        val pointcut = MyPointcut()

        val advisor = DefaultPointcutAdvisor(pointcut, advice)

        proxyFactory.addAdvisor(advisor)
        val proxy = proxyFactory.proxy as ServiceInterface

        proxy.save()
        /**
         * 11:13:22.155 [Test worker] INFO com.example.demo.proxy.advisor.MyMethodMatcher -- 포인트컷 호출 method={save}, targetClass={class com.example.demo.proxy.common.service.ServiceImpl}
         * 11:13:22.158 [Test worker] INFO com.example.demo.proxy.advisor.MyMethodMatcher -- 포인트컷 결과. result={true}
         * 11:13:22.160 [Test worker] INFO com.example.demo.proxy.common.advice.TimeAdvice -- TimeProxy - TimeAdvice 실행
         * 11:13:22.160 [Test worker] INFO com.example.demo.proxy.common.service.ServiceImpl -- ServiceImpl save 호출
         * 11:13:22.161 [Test worker] INFO com.example.demo.proxy.common.advice.TimeAdvice -- execTime={0}
         * 11:13:22.161 [Test worker] INFO com.example.demo.proxy.common.advice.TimeAdvice -- TimeProxy - TimeAdvice 종료
         * */

        proxy.find()
        /**
         * 11:14:09.288 [Test worker] INFO com.example.demo.proxy.advisor.MyMethodMatcher -- 포인트컷 호출 method={toString}, targetClass={class com.example.demo.proxy.common.service.ServiceImpl}
         * 11:14:09.288 [Test worker] INFO com.example.demo.proxy.advisor.MyMethodMatcher -- 포인트컷 결과. result={false}
         * 11:14:16.245 [Test worker] INFO com.example.demo.proxy.advisor.MyMethodMatcher -- 포인트컷 호출 method={find}, targetClass={class com.example.demo.proxy.common.service.ServiceImpl}
         * 11:14:16.245 [Test worker] INFO com.example.demo.proxy.advisor.MyMethodMatcher -- 포인트컷 결과. result={false}
         * 11:14:16.246 [Test worker] INFO com.example.demo.proxy.common.service.ServiceImpl -- ServiceImpl find 호출
         *
         */

        /**
         * 결과: find() 호출 시, advice에서 해주는 execTime을 로그에 남기는 기능이 작동하지 않았다.
         * */
    }


    @Test
    @DisplayName("직접 만드는 pointcut2")
    fun advisorTest3() {
        val target = ConcreteService()
        val proxyFactory = ProxyFactory(target)
        proxyFactory.isProxyTargetClass = true

        val advice = TimeAdvice()
        val pointcut = MyPointcut()

        val advisor = DefaultPointcutAdvisor(pointcut, advice)

        proxyFactory.addAdvisor(advisor)
        val proxy = proxyFactory.proxy as ConcreteService

        proxy.call()
        /**
         * 11:42:12.101 [Test worker] INFO com.example.demo.proxy.advisor.MyClassFilter -- MyClassFilter. matched={false}, filteredReason={Interface가 없습니다.}
         * 11:42:12.103 [Test worker] INFO com.example.demo.proxy.advisor.MyClassFilter -- MyClassFilter. matched={false}, filteredReason={Interface가 없습니다.}
         * 11:42:12.103 [Test worker] INFO com.example.demo.proxy.advisor.MyClassFilter -- MyClassFilter. matched={false}, filteredReason={Interface가 없습니다.}
         * */
    }

    @Test
    @DisplayName("스프링이 제공하는 포인트컷")
    fun advisorTest4() {
        /**
         * 스프링이 제공하는 포인트컷:
         *      NameMatchMethodPointcut
         *      JdkRegexMethodPointcut
         *      TruePointcut: 항상 true 반환 -> 모든 클래스, 모든 메소드에 대해 advice 수행
         *      AnnotationMatchingPointcut
         *      AspectJExpressionPointcut. <- 이게 실무에서 가장 많이 사용되게 된다.
         *
         * */
        val target: ServiceInterface = ServiceImpl()
        val proxyFactory = ProxyFactory(target)

        val advice = TimeAdvice()
        val pointcut = NameMatchMethodPointcut()
        // 메소드명이 save인 경우에만, advice 적용
        pointcut.setMappedName("save")
        val advisor = DefaultPointcutAdvisor(pointcut, advice)

        proxyFactory.addAdvisor(advisor)
        val proxy = proxyFactory.proxy as ServiceInterface

        proxy.save()
        /**
         * 11:49:54.644 [Test worker] INFO com.example.demo.proxy.common.advice.TimeAdvice -- TimeProxy - TimeAdvice 실행
         * 11:49:54.646 [Test worker] INFO com.example.demo.proxy.common.service.ServiceImpl -- ServiceImpl save 호출
         * 11:49:54.647 [Test worker] INFO com.example.demo.proxy.common.advice.TimeAdvice -- execTime={0}
         * 11:49:54.647 [Test worker] INFO com.example.demo.proxy.common.advice.TimeAdvice -- TimeProxy - TimeAdvice 종료
         * */
        proxy.find()
    }
}
