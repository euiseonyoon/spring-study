package com.example.demo.aop.proxyvs

import com.example.demo.aop.order.aop.member.MemberService
import com.example.demo.aop.order.aop.member.MemberServiceImpl
import com.example.demo.aop.proxyvs.code.ProxyDiAspect
import com.example.demo.common.logger
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

// @SpringBootTest(properties = ["spring.aop.proxy-target-class=false"]) // target이 interface를 가지고 있으면 JDK 동적 프록시 생성
@SpringBootTest(properties = ["spring.aop.proxy-target-class=true"])
@Import(ProxyDiAspect::class)
class ProxyDiTest {
    private val log = logger()

    @Autowired
    lateinit var memberService: MemberService

    // spring.aop.proxy-target-class=true일때 가능
    @Autowired
    lateinit var memberServiceImpl: MemberServiceImpl

    /**
     *
     *     @Autowired
     *     lateinit var memberServiceImpl: MemberServiceImpl
     *     위 라인처럼 memberServiceImpl을 주입 받으려고 하면 아래와 같은 오류가 발생한다.
     *
     *     Bean named 'memberServiceImpl' is expected to be of type 'com.example.demo.aop.order.aop.member.MemberServiceImpl' but was actually of type 'jdk.proxy3.$Proxy78'
     *
     *     우리는 MemberServiceImpl을 기대했는데 실제로 주입되려고 전달된건 `jdk.proxy3.$Proxy78`
     *
     *     왜냐면, memberService는 JDK proxy임
     *
     *     우리가
     *     @Autowired
     *     lateinit var memberServiceImpl: MemberServiceImpl
     *     이렇게 해서 memberServiceImpl 을 주입 받고자 하면, JDK 동적 proxy의 target을 구체 클래스로 CASTING 해야한다
     *     히지만 jdk proxy는 MemberService라는 인터페이스만 알고 있고, MemberServiceImpl이라는 타입에 대해 전혀 모른다.
     *     이러한 이유로 오류가 발생한다.
     *
     *     결론:
     *     JDK 동적 프록시인 bean을 주입받으려면 구체클래스 타입(target의 클래스 타입)으로는 안된다. interface 타입으로 주입해야한다.
     * */


    @Test
    fun test() {
        // spring.aop.proxy-target-class의 값과 상관없이 성공
        log.info("memberService class={}", memberService.javaClass)

        // spring.aop.proxy-target-class=true 일때만 성공
        // CGLIB 프록시는 target(구체 클래스)를 상속받아서 만든다. 당연히 자식->부모로 캐스팅가능
        log.info("memberServiceImpl class={}", memberServiceImpl.javaClass)
        memberService.hello("hi")

        // 이러한 이유로 spring.aop.proxy-target-class=true가 default
        // 본래 CGLIB 프록시를 사용하기 위해서는 기본 생성자가 있어야 한다는 단점이 있었지만
        // 스프링 2.0 이후 버전에서 해결되었다고 한다.
        // 하지만 아직도 final 클래스는 상속이 되지 않는 이슈로 CGLIB 프록시 생성이 안된다.
        // 이는 kotlin을 사용하는데 치명적이다. 하지만 kotlin("plugin.spring") 을 포함시키면 open을 붙여준다
        // 이러한 이유로 @OpenClass 라는 어노테이션도 직접 만들어서 사용했다
    }
}