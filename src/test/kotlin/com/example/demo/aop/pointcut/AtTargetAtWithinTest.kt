package com.example.demo.aop.pointcut

import com.example.demo.aop.order.aop.member.annotation.ClassAop
import com.example.demo.common.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.junit.jupiter.api.Test
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

/**
 * NOTE:
 *   @Target:  인스턴스의 모든 메서드를 조인포인트로 적용한다 ( 타켓의 부모의 메소드 까지)
 *   @Within:  해당 타입내의 메서드만 조인포인트로 적용한다 ( 타켓의 부모 메소드는 제외)
 *
 *   주의!!:
 *      args, @args, @target 의 포인트컷 지시자는 단독으로 사용하면 안된다!!
 *      1. 위의 포인트컷 지시자는 실제 객체 인스턴스(프록시 인스턴스) 가 생성되고, 실행될때에 어드바이저 적용여부를 확인할 수 있다.
 *      2. 프록시 인스턴스가 없다면 포인트컷 적용여부 자체를 판단이 불가능하다.
 *      3. 그러면 위의 포인트컷이 단독으로 사용되면 스프링은 모든 빈에 AOP를 적용하려고 한다.
 *      4. 이떄 만약 내부적으로 final인 빈들도 있다면 에러가 발생한다.
 *      5. 따라서 반드시 (execute)와 같이 프록시 적용대상을 최소/축소 하는 표현식과 같이 사용되어야 한다.
 * */
@SpringBootTest
@Import(AtTargetAtWithinTest.Config::class)
class AtTargetAtWithinTest {
    private val log = logger()

    // NOTE: 클래스, 메소드 모두 open임을 확인하자!!!
    open class Parent {
        open fun parentMethod(){} /* 부모에만 있는 메서드 */
    }

    @ClassAop
    open class Child : Parent() {
        open fun childMethod() {}
    }

    @Aspect
    class AtTargetAtWithinAspect {
        private val log = logger()

        // @target: 인스턴스 기준으로 모든 메서드의 조인 포인트를 선정, 부모 타입의 메서드도 적용
        @Around("execution(* com.example.demo.aop.pointcut..*(..)) && @target(com.example.demo.aop.order.aop.member.annotation.ClassAop)")
        fun atTarget(joinPoint: ProceedingJoinPoint): Any? {
            log.info("[@target] {}", joinPoint.signature)
            return joinPoint.proceed()
        }

        //@within: 선택된 클래스 내부에 있는 메서드만 조인 포인트로 선정, 부모 타입의 메서드는 적용되지 않음
        @Around("execution(* com.example.demo.aop.pointcut..*(..)) && @within(com.example.demo.aop.order.aop.member.annotation.ClassAop)")
        fun atWithin(joinPoint: ProceedingJoinPoint): Any? {
            log.info("[@within] {}", joinPoint.signature)
            return joinPoint.proceed()
        }
    }

    open class Config {
        @Bean
        fun parent() = Parent()

        @Bean
        fun child() = Child()

        @Bean
        fun atTargetAtWithinAspect(): AtTargetAtWithinAspect = AtTargetAtWithinAspect()
    }

    @Autowired
    lateinit var child: Child

    @Test
    fun success() {
        log.info("          START          ")
        log.info("-----------------------")
        log.info("child Proxy={}", child::class)
        log.info("-----------------------")
        child.childMethod() //부모, 자식 모두 있는 메서드
        log.info("-----------------------")
        log.info("-----------------------")
        child.parentMethod() //부모 클래스만 있는 메서드
        
        /**
         *  c.e.d.aop.pointcut.AtTargetAtWithinTest  :           START
         *  c.e.d.aop.pointcut.AtTargetAtWithinTest  : -----------------------
         *  c.e.d.aop.pointcut.AtTargetAtWithinTest  : child Proxy=class com.example.demo.aop.pointcut.AtTargetAtWithinTest$Child$$SpringCGLIB$$0
         *  c.e.d.aop.pointcut.AtTargetAtWithinTest  : -----------------------
         *  argetAtWithinTest$AtTargetAtWithinAspect : [@target] void com.example.demo.aop.pointcut.AtTargetAtWithinTest$Child.childMethod().
         *  argetAtWithinTest$AtTargetAtWithinAspect : [@within] void com.example.demo.aop.pointcut.AtTargetAtWithinTest$Child.childMethod().
         *  c.e.d.aop.pointcut.AtTargetAtWithinTest  : -----------------------
         *  c.e.d.aop.pointcut.AtTargetAtWithinTest  : -----------------------
         *  argetAtWithinTest$AtTargetAtWithinAspect : [@target] void com.example.demo.aop.pointcut.AtTargetAtWithinTest$Parent.parentMethod()
         *
         *  child에는 parentMethod()가 정의 되어 있지 않았기 떄문에 `[@within] .. parentMethod()` 가 출력되지 않았다.
         *
         * */
    }
}
