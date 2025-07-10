package com.example.demo.aop.order.aop

import com.example.demo.common.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.core.annotation.Order

/***
 *
 * Advice는 기본적으로 순서보장을 하지 않는다!!
 * 순서를 지정하고 싶으면 @Aspect 어노테이션에 @Order를 사용하면된다.
 * 하지만! 같은 @Aspect 내부에 있는 advice의 순서를 지저할 수 없다
 * 한 마디로 Advice들 순서는 advice들을 @Aspect + @Order로 다 나누어 처리 할 수 밖에 없다.
 *
 */
class AspectV5Order {
    private val log = logger()

    @Aspect
    @Order(2)
    class LogAspect {
        private val log = logger()

        @Around("com.example.demo.aop.order.aop.Pointcuts.allOrder()")
        fun doLog(jointPoint: ProceedingJoinPoint): Any? {
            log.info("[log] {}", jointPoint.signature)
            return jointPoint.proceed()
        }
    }

    @Aspect
    @Order(1)
    class TransactionAspect{
        private val log = logger()

        @Around("com.example.demo.aop.order.aop.Pointcuts.orderAndService()") // &&, ||, ! 가능
        fun doTransaction(jointPoint: ProceedingJoinPoint): Any? {
            try {
                log.info("[(Mock) 트렌젝션 시작] {}", jointPoint.signature)
                val result = jointPoint.proceed()
                log.info("[(Mock) 트렌젝션 커밋] {}", jointPoint.signature)
                return result
            } catch (e: Exception) {
                log.info("[(Mock) 트렌젝션 롤백] {}", jointPoint.signature)
                throw e
            } finally {
                log.info("[리소스 릴리즈] {}", jointPoint.signature)
            }
        }
    }
    /**
     *
     *  [    Test worker] .d.a.o.a.AspectV5Order$TransactionAspect : [(Mock) 트렌젝션 시작] void com.example.demo.aop.order.OrderService.orderItem(String). <- doTransaction() 이 먼저 찍히고
     *  [    Test worker] c.e.d.a.o.aop.AspectV5Order$LogAspect    : [log] void com.example.demo.aop.order.OrderService.orderItem(String) <- doLog() 가 나중에 찍힘.
     *  [    Test worker] c.example.demo.aop.order.OrderService    : [orderService] 실행
     *  [    Test worker] c.e.d.a.o.aop.AspectV5Order$LogAspect    : [log] String com.example.demo.aop.order.OrderRepository.save(String)
     *  [    Test worker] c.e.demo.aop.order.OrderRepository       : [orderRepository] 실행
     *  [    Test worker] .d.a.o.a.AspectV5Order$TransactionAspect : [(Mock) 트렌젝션 커밋] void com.example.demo.aop.order.OrderService.orderItem(String)
     *  [    Test worker] .d.a.o.a.AspectV5Order$TransactionAspect : [리소스 릴리즈] void com.example.demo.aop.order.OrderService.orderItem(String)
     *  [    Test worker] com.example.demo.aop.AopTest             : isAopProxy, orderService=true
     *  [    Test worker] com.example.demo.aop.AopTest             : isAopProxy, orderRepository=true
     *
     * */
}
