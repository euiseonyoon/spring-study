package com.example.demo.aop.pointcut

import com.example.demo.aop.order.aop.member.MemberService
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
@Import(AtAnnotationTest.AtAnnotationAspect::class)
class AtAnnotationTest {
    private val log = logger()

    @Autowired
    lateinit var memberService: MemberService

    @Test
    fun success() {
        log.info("memberService proxy={}", memberService)
        memberService.hello("hello there")

        /**
         * c.e.demo.aop.pointcut.AtAnnotationTest   : memberService proxy=com.example.demo.aop.order.aop.member.MemberServiceImpl@399f5daf
         * .a.p.AtAnnotationTest$AtAnnotationAspect : [@annotation] String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String)
         *
         * */
    }

    @Aspect
    open class AtAnnotationAspect {
        private val log = logger()

        @Around("@annotation(com.example.demo.aop.order.aop.member.annotation.MethodAop)")
        fun doAtAnnotation(joinPoint: ProceedingJoinPoint) : Any? {
            log.info("[@annotation] {}", joinPoint.signature)
            return joinPoint.proceed()
        }
    }
}