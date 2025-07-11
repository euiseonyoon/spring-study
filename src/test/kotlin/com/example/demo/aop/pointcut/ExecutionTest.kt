package com.example.demo.aop.pointcut

import com.example.demo.aop.order.aop.member.MemberService
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
//         execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)
//         modifiers-pattern? : public (생략가능하지만 넣어줌)
//         ret-type-pattern: String
//         declaring-type-pattern?: com.example.demo.aop.order.aop.member.MemberServiceImpl
//         name-pattern: hello
//         param-patter: String
//         throws-pattern?: 생략
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

    @Test
    fun typeExactMatch() {
        pointcut.expression = "execution(* com.example.demo.aop.order.aop.member.MemberServiceImpl.*(..))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun typeMatchSuperType() {
        // MemberServiceImpl은 MemberService 인터페이스를 구현함
        // MemberService 로만 찾아도 포인트컷 적용됨
        pointcut.expression = "execution(* com.example.demo.aop.order.aop.member.MemberService.*(..))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }

        pointcut.expression = "execution(* com.example.demo.aop.order.aop.member.MemberServiceImpl.*(..))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberService::class.java)
        }
    }

    @Test
    fun typeMatchInternal() {
        pointcut.expression = "execution(* com.example.demo.aop.order.aop.member.MemberServiceImpl.*(..))"
        val internalMethod = MemberServiceImpl::class.java.getMethod("internal", String::class.java)
        assertTrue {
            pointcut.matches(internalMethod, MemberServiceImpl::class.java)
        }
    }

    @Test
    fun typeMatchNoSuperTypeMethodFalse() {
        // MemberServiceImpl(자식)은 MemberService(부모) 인터페이스를 구현함
        pointcut.expression = "execution(* com.example.demo.aop.order.aop.member.MemberService.*(..))"
        val internalMethodOnlyOnChildNotOnParent = MemberServiceImpl::class.java.getMethod("internal", String::class.java)

        // internalMethodOnlyOnChildNotOnParent (MemberServiceImpl.internal() 메소드는 부모인 MemberService에는 정의 되어있지 않다)
        assertFalse {
            pointcut.matches(internalMethodOnlyOnChildNotOnParent, MemberServiceImpl::class.java)
        }
    }

    // String 타입의 파라미터를 허용
    @Test
    fun argsMatch() {
        pointcut.expression = "execution(* *(String))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    // 파라미터가 없는 것
    @Test
    fun argsMatchNoArgs() {
        pointcut.expression = "execution(* *())"
        assertFalse {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    // 정확히 하나의 파라미터만 허용, 하지만 모든 파라미터 타입 허용
    @Test
    fun argsMatchStar() {
        pointcut.expression = "execution(* *(*))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    // 파라미터에 몇 개 들어와도 파라미터 타입상관없이 모두 허용
    // e,g, (), (param), (param1, param2) ...
    @Test
    fun argsMatchAll() {
        pointcut.expression = "execution(* *(**))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }


    // 파라미터에 String param으로 시작하고, 그 후에는 파라미터가 몇 개 들어와도 파라미터 타입상관없이 모두 허용
    // e,g, (String), (String, param), (String, param1, param2) ...
    @Test
    fun argsMatchComplex() {
        pointcut.expression = "execution(* *(String, ..))"
        assertTrue {
            pointcut.matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }
}
