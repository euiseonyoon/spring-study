package com.example.demo.proxy.advisor

import com.example.demo.common.logger
import org.springframework.aop.MethodMatcher
import java.lang.reflect.Method

class MyMethodMatcher : MethodMatcher {
    private val log = logger()
    private val MATCH_NAME = "save"

    override fun matches(method: Method, targetClass: Class<*>): Boolean {
        // save() 가 호출된 경우만 advice를 적용해보자.
        val result = method.name.equals(MATCH_NAME)
        log.info("포인트컷 호출 method={${method.name}}, targetClass={${targetClass}}")
        log.info("포인트컷 결과. result={$result}")
        return result
    }

    override fun isRuntime(): Boolean {
        /**
         * return false 이면 위의  matches()가 호출
         *   -> 위의 matches()를 사용하면 정적인 정보만 사용하기 때문에 스프링이 내부에서 캐싱을 통해
         *          *      성능 향상이 가능하다.
         *   -> 위 matches()를 사용하게 되면 파라미터 args가 동적으로 변경된다고 가정, 내부 캐싱을 하지 않음
         *   -> 그래서 isRuntime()의 return을 false
         * return true 이면 아래의 matches()가 호출
         * */
        return false
    }

    override fun matches(
        method: Method,
        targetClass: Class<*>,
        vararg args: Any?
    ): Boolean {
        throw UnsupportedOperationException("지원이 되지 않습니다.")
    }
}
