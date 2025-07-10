package com.example.demo.config.v4_postprocessor.postprocesor

import com.example.demo.common.logger
import org.springframework.aop.Advisor
import org.springframework.aop.framework.ProxyFactory
import org.springframework.beans.factory.config.BeanPostProcessor

class PackageLogTracePostProcessor(
    // 특정 package 하위의 bean들만 처리할 예정
    private val basePackage: String,
    private val advisor: Advisor
): BeanPostProcessor {
    private val log = logger()

    // BEAN 객체가 초기화 된 후에, PROXY를 적용할 예정
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        log.info("param beanName={}, bean={}", beanName, bean)

        // 프록시 적용 대상 여부 체크

        // 프록시 적용  대상이 아니면 원본을 그대로 진행
        if (!bean.javaClass.packageName.startsWith(basePackage)) {
            // basePackage의 bean 아닌 이상, bean에 proxy 적용안하고 그대로 반환
            return bean
        }

        // basePackage의 객체이므로 원본(객체)을 타겟으로 하고 전달된 advisor가 적용된 프록시를 만듬
        val proxyFactory = ProxyFactory(bean)
        proxyFactory.addAdvisor(advisor)
        val proxy = proxyFactory.proxy

        log.info("create proxy: target={}, proxy={}", bean.javaClass, proxy.javaClass)
        return proxy
    }
}
