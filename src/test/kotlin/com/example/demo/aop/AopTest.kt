package com.example.demo.aop

import com.example.demo.aop.order.OrderRepository
import com.example.demo.aop.order.OrderService
import com.example.demo.aop.order.aop.AspectV1
import com.example.demo.aop.order.aop.AspectV2
import com.example.demo.aop.order.aop.AspectV3
import com.example.demo.aop.order.aop.AspectV4Pointcut
import com.example.demo.aop.order.aop.AspectV5Order
import com.example.demo.aop.order.aop.AspectV6Advice
import com.example.demo.common.logger
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
// @Import(AspectV1::class)
// @Import(AspectV2::class)
// @Import(AspectV3::class)
// @Import(AspectV4Pointcut::class)
// @Import(AspectV5Order.LogAspect::class, AspectV5Order.TransactionAspect::class)
@Import(AspectV6Advice::class)
class AopTest {
    private val log = logger()

    @Autowired
    lateinit var orderService: OrderService

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Test
    fun aopInfo() {
        log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderService))
        log.info("isAopProxy, orderRepository={}", AopUtils.isAopProxy(orderRepository))
    }

    @Test
    fun success() {
        orderService.orderItem("item")
    }

    @Test
    fun exception() {
        assertThrows<IllegalStateException>{ orderService.orderItem("ex") }
    }

}