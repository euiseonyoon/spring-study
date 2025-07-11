package com.example.demo.aop.order.aop.member

import org.springframework.stereotype.Service


@Service
class MyTestService{
    fun test(test: String): String = "ThisIsTestService"
}