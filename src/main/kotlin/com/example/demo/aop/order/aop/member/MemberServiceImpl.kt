package com.example.demo.aop.order.aop.member

import com.example.demo.aop.order.aop.member.annotation.ClassAop
import com.example.demo.aop.order.aop.member.annotation.MethodAop
import com.example.demo.aop.order.aop.member.models.Student
import com.example.demo.config.dynmic_proxy.OpenClass
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@OpenClass
@ClassAop
@Component
class MemberServiceImpl : MemberService{

    @MethodAop("test value")
    override fun hello(param: String): String {
        return "ok"
    }

    fun internal(param: String): String {
        return "ok"
    }

    fun anyParamMethod(param: Any): String {
        return "any"
    }

    fun helloToStudent(student: Student): String {
        return "student"
    }
}
