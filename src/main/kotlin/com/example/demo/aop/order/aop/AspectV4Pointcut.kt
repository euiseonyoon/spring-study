package com.example.demo.aop.order.aop

import com.example.demo.common.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

@Aspect
class AspectV4Pointcut {
    private val log = logger()

    @Around("com.example.demo.aop.order.aop.Pointcuts.allOrder()")
    fun doLog(jointPoint: ProceedingJoinPoint): Any? {
        log.info("[log] {}", jointPoint.signature)
        return jointPoint.proceed()
    }

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

    /**
     * NOTE:
     * 잘 된다.
     *
     * 2025-07-10T21:11:14.972+09:00  INFO 59321 --- [demo] [    Test worker] c.example.demo.aop.order.aop.AspectV3    : [log] void com.example.demo.aop.order.OrderService.orderItem(String)
     * 2025-07-10T21:11:14.973+09:00  INFO 59321 --- [demo] [    Test worker] c.example.demo.aop.order.aop.AspectV3    : [(가쨔) 트렌젝션 시작] void com.example.demo.aop.order.OrderService.orderItem(String)
     * 2025-07-10T21:11:14.973+09:00  INFO 59321 --- [demo] [    Test worker] c.example.demo.aop.order.OrderService    : [orderService] 실행
     * 2025-07-10T21:11:14.973+09:00  INFO 59321 --- [demo] [    Test worker] c.example.demo.aop.order.aop.AspectV3    : [log] String com.example.demo.aop.order.OrderRepository.save(String)
     * 2025-07-10T21:11:14.973+09:00  INFO 59321 --- [demo] [    Test worker] c.e.demo.aop.order.OrderRepository       : [orderRepository] 실행
     * 2025-07-10T21:11:14.973+09:00  INFO 59321 --- [demo] [    Test worker] c.example.demo.aop.order.aop.AspectV3    : [(가쨔) 트렌젝션 커밋] void com.example.demo.aop.order.OrderService.orderItem(String)
     * 2025-07-10T21:11:14.973+09:00  INFO 59321 --- [demo] [    Test worker] c.example.demo.aop.order.aop.AspectV3    : [리소스 릴리즈] void com.example.demo.aop.order.OrderService.orderItem(String)
     *
     * 근데 순서가 항상 doLog -> doTransaction
     * */
}
