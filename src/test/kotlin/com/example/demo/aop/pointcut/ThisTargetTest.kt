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


@Import(ThisTargetTest.ThisTargetAspect::class)
@SpringBootTest(properties = ["spring.aop.proxy-target-class=false"]) //JDK 동적 프록시
// @SpringBootTest(properties = ["spring.aop.proxy-target-class=true"]) //CGLIB
class ThisTargetTest {
    private val log = logger()

    @Autowired
    lateinit var memberService: MemberService

    @Test
    fun success() {
        log.info("memberService Proxy={}", memberService.javaClass)
        memberService.hello("helloA")
    }

    @Aspect
    class ThisTargetAspect {
        val log = logger()

        //부모 타입 허용
        @Around("this(com.example.demo.aop.order.aop.member.MemberService)")
        fun doThisInterface(joinPoint: ProceedingJoinPoint): Any? {
            log.info("[this-interface] {}", joinPoint.signature)
            return joinPoint.proceed()
        }

        //부모 타입 허용
        @Around("target(com.example.demo.aop.order.aop.member.MemberService)")
        @Throws(Throwable::class)
        fun doTargetInterface(joinPoint: ProceedingJoinPoint): Any? {
            log.info("[target-interface] {}", joinPoint.signature)
            return joinPoint.proceed()
        }

        // 이건 아마 JDK 방식으로 생성된 프록시에는 적용이 안될거다. 아래의 포인트컷으로 지정할수 없어서..
        @Around("this(com.example.demo.aop.order.aop.member.MemberServiceImpl)")
        fun doThis(joinPoint: ProceedingJoinPoint): Any? {
            log.info("[this-impl] {}", joinPoint.signature)
            return joinPoint.proceed()
        }

        @Around("target(com.example.demo.aop.order.aop.member.MemberServiceImpl)")
        fun doTarget(joinPoint: ProceedingJoinPoint): Any? {
            log.info("[target-impl] {}", joinPoint.signature)
            return joinPoint.proceed()
        }
    }
    /**
     * CGLIB:
     * memberService Proxy=class com.example.demo.aop.order.aop.member.MemberServiceImpl$$SpringCGLIB$$0
     * [target-impl] String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String)  <- 실제 target
     * [target-interface] String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String)
     * [this-impl] String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String)
     * [this-interface] String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String) <- 실제 target
     *
     *
     * JDK 동적 프록시:
     * memberService Proxy=class jdk.proxy3.$Proxy73
     * [target-impl] String com.example.demo.aop.order.aop.member.MemberService.hello(String)
     * [target-interface] String com.example.demo.aop.order.aop.member.MemberService.hello(String) <- interface
     *  ~~~ [this-impl] 이 없다 !!
     * [this-interface] String com.example.demo.aop.order.aop.member.MemberService.hello(String) <- interface
     *
     * **/
}
