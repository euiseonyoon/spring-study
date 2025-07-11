package com.example.demo.aop.pointcut

import com.example.demo.aop.order.aop.member.MemberService
import com.example.demo.aop.order.aop.member.MemberServiceImpl
import com.example.demo.common.logger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import java.lang.reflect.Method
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class WithinTest {
    /**
     * NOTE:
     *    public java.lang.String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(java.lang.String)
     *
     *    execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)
     *    modifiers-pattern? : public (생략가능하지만 넣어줌)
     *    ret-type-pattern: String
     *    declaring-type-pattern?: com.example.demo.aop.order.aop.member.MemberServiceImpl. <- 이 부분을 정의하는데 within이 사용됨
     *    name-pattern: hello
     *    param-patter: String
     *    throws-pattern?: 생략
     *
     * Within은 execution의 declaring-type-pattern 부분을 정하는데 사용한다
     * e.g.) com.example.demo.aop.order.aop.member.MemberServiceImpl  <- 이 부분
     *
     *
     * 주의!!!!:
     *    within 사용시 표현식에 부모 타입을 지정하면 안된다!!
     *    정확하게 타입이 맞아야 한다.
     * */

    private val log = logger()

    private val pointcut = AspectJExpressionPointcut()
    private var helloMethod: Method? = null

    @BeforeEach
    fun init() {
        helloMethod = MemberServiceImpl::class.java.getMethod("hello", String::class.java)
    }

    @Test
    fun withinExact() {
        pointcut.expression = "within(com.example.demo.aop.order.aop.member.MemberServiceImpl)"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun withinStar() {
        pointcut.expression = "within(com.example.demo.aop.order.aop.member.*berService*)"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun withinSubPackage() {
        pointcut.expression = "within(com.example.demo.aop.order.aop.member.*)"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun withinSubPackageFail() {
        // aop 바로 하위 페키지만 적용하니 fail
        pointcut.expression = "within(com.example.demo.aop.order.aop.*)"
        assertFalse {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun withinAllSubPackage() {
        // aop 바로 하위 페키지만 적용하니 fail
        pointcut.expression = "within(com.example.demo.aop.order.aop..*)"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    @DisplayName("타켓의 정확한 타입에만 직접 적용됩니다, 인터페이스(부모)를 선정하면 안됩니다")
    fun withinSuperTypeFail() {
        // MemberServiceImpl은 MemberService 인터페이스를 구현함.
        pointcut.expression = "within(com.example.demo.aop.order.aop.member.MemberService)"
        assertFalse {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
        // 부모를 통해 선정하고 하고자 하면, execution을 사용합시다.
    }
}
