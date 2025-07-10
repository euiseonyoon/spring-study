package com.example.demo.proxy.advisor

import org.springframework.aop.ClassFilter
import org.springframework.aop.MethodMatcher
import org.springframework.aop.Pointcut

class MyPointcut: Pointcut {
    override fun getClassFilter(): ClassFilter {
        return MyClassFilter()
    }

    override fun getMethodMatcher(): MethodMatcher {
        return MyMethodMatcher()
    }
}

