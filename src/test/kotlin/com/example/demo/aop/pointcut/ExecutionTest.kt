package com.example.demo.aop.pointcut

import com.example.demo.aop.order.aop.member.MemberServiceImpl
import com.example.demo.common.logger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import java.lang.reflect.Method
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExecutionTest {

    private val log = logger()

    private val pointcut = AspectJExpressionPointcut()
    private var helloMethod: Method? = null

    @BeforeEach
    fun init() {
        helloMethod = MemberServiceImpl::class.java.getMethod("hello", String::class.java)
    }

    @Test
    fun printMethod() {
        // public java.lang.String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod={}", helloMethod)
    }

    @Test
    fun exactMatch() {
        // execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)
        // modifiers-pattern? : public (생략가능하지만 넣어줌)
        // ret-type-pattern: String
        // declaring-type-pattern?: com.example.demo.aop.order.aop.member.MemberServiceImpl
        // name-pattern: hello
        // param-patter: String
        // throws-pattern?: 생략
        pointcut.expression = "execution(public String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(String))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun allMatch() {
        // modifiers-pattern? : 생략
        // ret-type-pattern: * (리턴 타입 상관없음)
        // declaring-type-pattern?: 생략
        // name-pattern: * (메소드 이름 상관없음)
        // param-patter: .. (모든것 가능)
        // throws-pattern?: 생략
        pointcut.expression = "execution(* * (..))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun nameMatch() {
        pointcut.expression = "execution(* hello (..))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun nameMatchPatternMatching() {
        pointcut.expression = "execution(* hel* (..))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }

        pointcut.expression = "execution(* *ell* (..))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun nameMatchFalse() {
        pointcut.expression = "execution(* hellll* (..))"
        assertFalse {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }

        pointcut.expression = "execution(* *eell* (..))"
        assertFalse {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun packageExactMatch1() {
        pointcut.expression = "execution(* com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(..))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun packageExactMatch2() {
        // com.example.demo.aop.order.aop.member 바로 하위의 모든 페키지의 모든 메소드
        // e.g. com.example.demo.aop.order.aop.member.something. <- ok
        // e.g. com.example.demo.aop.order.aop.member.something.other <- 적용안됨
        pointcut.expression = "execution(* com.example.demo.aop.order.aop.member.*.*(..))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun packageMatchFail() {
        // com.example.demo.aop.order.aop. 바로 하위 페키지의 모든 메소드
        pointcut.expression = "execution(* com.example.demo.aop.order.aop.*.*(..))"
        assertFalse {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun packageMatchSubPackage1() {
        // com.example.demo.aop.order.aop.member 하위 모든 페키지의 모든 메소드
        pointcut.expression = "execution(* com.example.demo.aop.order.aop.member..*.*(..))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun packageMatchSubPackage2() {
        // com.example.demo.aop 하위 모든 페키지의 모든 메소드
        pointcut.expression = "execution(* com.example.demo.aop..*.*(..))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }
}
