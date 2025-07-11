package com.example.demo.aop.pointcut

import com.example.demo.aop.order.aop.member.MemberServiceImpl
import com.example.demo.common.logger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import java.lang.reflect.Method
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class ArgsTest {
    /**
     * NOTE:
     *    public java.lang.String com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(java.lang.String)
     *
     *    execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)
     *    modifiers-pattern? : public (생략가능하지만 넣어줌)
     *    ret-type-pattern: String
     *    declaring-type-pattern?: com.example.demo.aop.order.aop.member.MemberServiceImpl.
     *    name-pattern: hello
     *    param-pattern: String. <- 이 부분을 정의하는데 args가 사용됨
     *    throws-pattern?: 생략
     *
     * Args은 execution의 param-patter 부분을 정하는데 사용한다
     * e.g.) com.example.demo.aop.order.aop.member.MemberServiceImpl.hello(  java.lang.String  <- 이 부분 )
     *
     *  execution은 param-pattern 을 통해 찾을때, 클래스에 선언된 정보를 기반 그대로 판단한다.
     *  e.g.) someMethod(String) 라면 (String) 으로 해야함
     *  args는 파라미터의 객체 인스턴스를 보고 판단한다. 부모 타입도 허용한다.
     *  e.g.) someMethod(String) 라면 (Object)로 해도 적용대상으로 인식한다
     *
     *  주의!!!  `Any`는 kotlin 타입이므로 args애 사용이 안됨을 알아두자
     *
     * */

    private val log = logger()

    private val pointcut = AspectJExpressionPointcut()
    private var helloMethod: Method? = null
    private var anyParamMethod: Method? = null

    @BeforeEach
    fun init() {
        helloMethod = MemberServiceImpl::class.java.getMethod("hello", String::class.java)
        anyParamMethod = MemberServiceImpl::class.java.getMethod("anyParamMethod", Any::class.java)
    }

    private fun pointcut(expression: String): AspectJExpressionPointcut {
        val pointcut = AspectJExpressionPointcut()
        pointcut.expression = expression
        return pointcut
    }

    @Test
    fun args() {
        //hello(String)과 매칭
        assertTrue {
            pointcut("args(String)")
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }
        assertTrue {
            pointcut("args(Object)")
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }
        assertFalse {
            pointcut("args()") // <- param이 없는건 안되겟죠.
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }
        assertTrue {
            pointcut("args(..)")
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }
        assertTrue {
            pointcut("args(*)")
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }
        assertTrue {
            pointcut("args(String,..)")
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    /**
     * execution(* *(java.io.Serializable)): 메서드의 시그니처로 판단 (정적)
     * args(java.io.Serializable): 런타임에 전달된 인수로 판단 (동적)
     */
    @Test
    fun argsVsExecution() {
        //Args
        assertTrue {
            pointcut("args(String)")
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }
        assertTrue {
            pointcut("args(java.io.Serializable)")
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }
        assertTrue {
            pointcut("args(Object)")
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }

        //Execution
        assertTrue {
            pointcut("execution(* *(String))")
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }
        assertFalse {
            pointcut("execution(* *(java.io.Serializable))") //매칭 실패
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }
        assertFalse {
            pointcut("execution(* *(Object))") //매칭 실패
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }
    }

    // 주의!!!  `Any`는 kotlin 타입이므로 args애 사용이 안됨을 알아두자
    @Test
    fun tryAny() {
        // java.lang.IllegalArgumentException: warning no match for this type name: Any [Xlint:invalidAbsoluteTypeName]
        assertThrows<IllegalArgumentException> {
            pointcut("args(Any)")
                .matches(helloMethod!!, MemberServiceImpl::class.java)
        }

        assertTrue {
            pointcut("args(Object)")
                .matches(anyParamMethod!!, MemberServiceImpl::class.java)
        }
    }

}
