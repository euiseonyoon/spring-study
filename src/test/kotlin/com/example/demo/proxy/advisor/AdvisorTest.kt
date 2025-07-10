package com.example.demo.proxy.advisor

import com.example.demo.proxy.common.advice.TimeAdvice
import com.example.demo.proxy.common.service.ServiceImpl
import com.example.demo.proxy.common.service.ServiceInterface
import org.junit.jupiter.api.Test
import org.springframework.aop.Pointcut
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.DefaultPointcutAdvisor

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
}