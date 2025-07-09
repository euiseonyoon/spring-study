package com.example.demo.proxy.common.service

import com.example.demo.common.logger

class ServiceImpl: ServiceInterface {
    private val log = logger()

    override fun save() {
        log.info("ServiceImpl save 호출")
    }

    override fun find() {
        log.info("ServiceImpl find 호출")
    }
}
