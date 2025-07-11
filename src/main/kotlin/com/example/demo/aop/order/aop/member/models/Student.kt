package com.example.demo.aop.order.aop.member.models

import com.example.demo.aop.order.aop.member.annotation.ParamAop

@ParamAop
data class Student(
    val name: String,
    val age: Int,
)
