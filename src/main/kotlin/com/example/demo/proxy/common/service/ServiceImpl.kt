package com.example.demo.proxy.common.service

class ServiceImpl: ServiceInterface {
    override fun save() {
        println("save 호출")
    }

    override fun find() {
        println("find 호출")
    }
}