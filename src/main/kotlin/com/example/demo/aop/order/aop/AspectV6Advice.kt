package com.example.demo.aop.order.aop

import com.example.demo.common.logger
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*


/**
 * 아래에 잘 표현되어 있다.
 * Around : 가장 강력하다. proceed를 통해서 조인포인트 실행도 할 수 있다.
 * Before: 조인 포인트 실행 이전에 실행
 * AfterRunning: 조인 포인트 실행 이후에 실행
 * AfterThrowing: 조인포인트가 예외를 던질때 실행
 * After: 조인포인트의 실행 -> 정상작동 or 예외상황 상관없이 실행
 *
 * 스프링 5.2.7 부터 같은 joinpoint 내에서
 * Around > Before > After > AfterReturning > AfterThrowing 순으로 advice가 적용된다.
 * 적용(호출)은 저 순으로 되지만, 호출 순서와 리턴 순서는 다르다.
 * 리턴 순서:
 * Around > Before > AfterThrowing > AfterThrowing > After
 *
 * */
@Aspect
class AspectV6Advice {
    private val log = logger()

    @Around("com.example.demo.aop.order.aop.Pointcuts.Pointcuts.orderAndService()")
    @Throws(Throwable::class)
    fun doTransaction(joinPoint: ProceedingJoinPoint): Any? {
        try {
            //@Before
            log.info("[around][트랜잭션 시작] {}", joinPoint.signature)
            val result = joinPoint.proceed()
            //@AfterReturning
            log.info("[around][트랜잭션 커밋] {}", joinPoint.signature)
            return result
        } catch (e: Exception) {
            //@AfterThrowing
            log.info("[around][트랜잭션 롤백] {}", joinPoint.signature)
            throw e
        } finally {
            //@After
            log.info("[around][리소스 릴리즈] {}", joinPoint.signature)
        }
    }

    @Before("com.example.demo.aop.order.aop.Pointcuts.orderAndService()")
    fun doBefore(joinPoint: JoinPoint) {
        // NOTE:
        // joinPoint.getThis() -> 프록시 객체를 반환
        // joinPoint.getTarget() -> target 객체를 반환
        // joinPoint.getArgs(), .toString() 등등의 기능을 제공

        log.info("[before] {}", joinPoint.signature)
    }

    @AfterReturning(value = "com.example.demo.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    fun doReturn(joinPoint: JoinPoint, result: Any?) {
        // returning = "result" 라고 하면 parameter의 result에 매칭되어 조인포인트.proceed() 결과 값을 받을 수 있다.
        // 하지만  result를 바꿀순 없다.
        // 하지만 강력한 Around에서는 val result = joinPoint.proceed() 후 result를 바꿀수 있다. 또한 exception도 바꿀수 있다.
        // Around에서는  joinpoint.proceed() 여러번도 가능하다. (재시도)
        log.info("[return] {} return={}", joinPoint.signature, result)
    }

    @AfterThrowing(value = "com.example.demo.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    fun doThrowing(joinPoint: JoinPoint, ex: java.lang.Exception) {
        // throwing = "ex" 라고 하면 parameter의 ex 매칭되어 던져진 exception확인 가능 하다.
        log.info(
            "[ex] {} message={}", joinPoint.signature,
            ex.message
        )
    }

    @After(value = "com.example.demo.aop.order.aop.Pointcuts.orderAndService()")
    fun doAfter(joinPoint: JoinPoint) {
        log.info("[after] {}", joinPoint.signature)
    }
}