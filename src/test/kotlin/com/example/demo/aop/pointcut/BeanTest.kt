package com.example.demo.aop.pointcut

import com.example.demo.aop.order.OrderService
import com.example.demo.common.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

/**
 * NOTE:
 *   빈 이름으로 포인트컷을 찾을때 사용된다. (AspectJ의 문법은 아니고. 스프링 고유)
 * */
@Import(BeanTest.BeanAspect::class)
@SpringBootTest
class BeanTest {
    @Autowired
    lateinit var orderService: OrderService

    @Test
    fun success() {
        orderService.orderItem("itemA")

        /**
         *  [bean] void com.example.demo.aop.order.OrderService.orderItem(String)
         *  [orderService] 실행
         *  [bean] String com.example.demo.aop.order.OrderRepository.save(String)
         *  [orderRepository] 실행
         *
         * */
    }

    @Aspect
    open class BeanAspect {
        private val log = logger()

        @Around("bean(orderService) || bean(*Repository)")
        fun doLog(joinPoint: ProceedingJoinPoint): Any? {
            log.info("[bean] {}", joinPoint.signature)
            return joinPoint.proceed()
        }
    }
}
