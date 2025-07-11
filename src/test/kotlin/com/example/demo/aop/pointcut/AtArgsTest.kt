package com.example.demo.aop.pointcut

import com.example.demo.aop.order.aop.member.MemberService
import com.example.demo.aop.order.aop.member.MemberServiceImpl
import com.example.demo.aop.order.aop.member.models.Student
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
 *  특정 annotation 들을 가진 메소드들을 포인트컷 으로 적용한다.
 *  e.g.) 현 코드에서는 @MethodAop
 *
 * */
@SpringBootTest
@Import(AtArgsTest.AtArgsAspect::class)
class AtArgsTest {
    private val log = logger()

    @Autowired
    lateinit var memberService: MemberServiceImpl

    @Test
    fun success() {
        log.info("memberService proxy={}", memberService)
        val student = Student("mike", 20)
        memberService.helloToStudent(student)

        /**
         * c.example.demo.aop.pointcut.AtArgsTest   : memberService proxy=com.example.demo.aop.order.aop.member.MemberServiceImpl@9147ba2
         * c.e.d.a.p.AtArgsTest$AtArgsAspect        : [@args] String com.example.demo.aop.order.aop.member.MemberServiceImpl.helloToStudent(Student)
         *
         * */
    }

    @Aspect
    open class AtArgsAspect {
        private val log = logger()

        // NOTE: args, @args, @target은 (executed)와 같이 적용대상을 최소화 하는것과 같이 쓰자!!
        @Around("execution(* com.example.demo.aop..*(..)) && @args(com.example.demo.aop.order.aop.member.annotation.ParamAop)")
        fun doAtAnnotation(joinPoint: ProceedingJoinPoint) : Any? {
            log.info("[@args] {}", joinPoint.signature)
            return joinPoint.proceed()
        }
    }
}