package com.example.demo.aop.pointcut

import com.example.demo.aop.order.aop.member.MemberService
import com.example.demo.aop.order.aop.member.annotation.ClassAop
import com.example.demo.aop.order.aop.member.annotation.MethodAop
import com.example.demo.common.logger
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(ParameterTest.ParameterAspect::class)
class ParameterTest {
    private val log = logger()

    @Autowired
    lateinit var memberService: MemberService

    @Test
    fun success() {
        log.info("memberService={}", memberService.javaClass)
        memberService.hello("something")
    }

    @Aspect
    open class ParameterAspect {
        private val log = logger()

        @Pointcut("execution(* com.example.demo.aop.order.aop.member..*.*(..))")
        private fun allMember(){}

        @Around("allMember()")
        fun logArgs1(joinPoint: ProceedingJoinPoint): Any? {
            val arg1 = joinPoint.args
            log.info("[logArgs1] {}, args={}", joinPoint.signature, arg1)
            // [logArgs1] String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String), args=[something]

            return joinPoint.proceed()
        }

        @Around("allMember() && args(someArg,..)")
        fun logArgs2(joinPoint: ProceedingJoinPoint, someArg: Any?): Any? {
            log.info("[logArgs2] {}, firstArg={}", joinPoint.signature, someArg)
            // [logArgs1] String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String), firstArg=something

            return joinPoint.proceed()
        }

        @Before("allMember() && args(someArg,..)")
        fun logArgs3(/* joinPoint: JoinPoint 해도 되고 안해도 되고*/ someArg: String ) {
            log.info("[logArgs3] firstArg={}", someArg)
            // [logArgs3] firstArg=something
        }

        @Before("allMember() && this(obj)")
        fun thisArgs(joinPoint: JoinPoint, obj: MemberService ) {
            log.info("[this] {}, obj={}", joinPoint.signature, obj.javaClass)
            // proxy 객체를 obj로 반환
            // [this] String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String),
            //        obj=class com.example.demo.aop.order.aop.member.MemberServiceImpl$$SpringCGLIB$$0
        }

        @Before("allMember() && target(obj)")
        fun targetArgs(joinPoint: JoinPoint, obj: MemberService ) {
            log.info("[target] {}, obj={}", joinPoint.signature, obj.javaClass)
            // 실제 객체(MemberServiceImpl)를 obj로 반환
            // [target] String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String),
            //          obj=class com.example.demo.aop.order.aop.member.MemberServiceImpl
        }

        @Before("allMember() && @target(annotation)")
        fun atTargetArgs(joinPoint: JoinPoint, annotation: ClassAop ) {
            log.info("[@target] {}, annotation={}", joinPoint.signature, annotation)
            // [@target] String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String), annotation=@com.example.demo.aop.order.aop.member.annotation.ClassAop()
        }

        @Before("allMember() && @within(annotation)")
        fun atWithinArgs(joinPoint: JoinPoint, annotation: ClassAop ) {
            log.info("[@within] {}, annotation={}", joinPoint.signature, annotation)
            // [@within] String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String), annotation=@com.example.demo.aop.order.aop.member.annotation.ClassAop()
        }

        @Before("allMember() && @annotation(annotation)")
        fun atAnnotationArgs(joinPoint: JoinPoint, annotation: MethodAop ) {
            log.info("[@annotation] {}, annotation={}", joinPoint.signature, annotation)
            // [@annotation] String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String), annotation=@com.example.demo.aop.order.aop.member.annotation.MethodAop("test value")
            // NOTE: 여기서 중요한건 MethodAop의 value를 꺼낼수 있음이다!!
            log.info("annotationValue={}", annotation.value)
            // annotationValue=test value
        }
    }
}
