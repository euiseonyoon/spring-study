package com.example.demo.proxy.common.service

import com.example.demo.config.dynmic_proxy.OpenClass

// 중요: Kotlin에서는 클래스는 기본 final임. 그래서 CGLIB 프록시를 생성 하기 위해서는 open을 추가해주어야 한다.
// 현재 plugin은 추가 되어있지만, 플러그인이 해당 클래스를 open처리 해야되는지 모르는 상황
// build.gradle.kts의 allOpen{} 을 참고
@OpenClass
class ConcreteService {
    fun call() {
        println("ConcreteService call()")
    }
}
