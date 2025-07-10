package com.example.demo.aop.order.aop

import com.example.demo.common.logger
import org.aspectj.lang.annotation.Pointcut

class Pointcuts {
    private val log = logger()

    @Pointcut("execution(* com.example.demo.aop.order..*(..))")
    fun allOrder(){}

    @Pointcut("execution(* *..*Service.*(..))")
    fun allService(){}

    @Pointcut("allOrder() && allService()")
    fun orderAndService(){}
}