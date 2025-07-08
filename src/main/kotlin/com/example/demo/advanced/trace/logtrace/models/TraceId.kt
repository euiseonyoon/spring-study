package com.example.demo.advanced.trace.logtrace.models

import java.util.UUID

data class TraceId(
    val id: String = createId(),
    var level: Int = 0
) {
    companion object {
        private fun createId(): String = UUID.randomUUID().toString().substring(0, 8)
    }

    private fun increaseLevel(): TraceId {
        this.level = this.level + 1
        return this
    }

    private fun decreaseLevel(): TraceId {
        this.level = this.level - 1
        return this
    }

    fun createNextId(): TraceId = increaseLevel()

    fun createPreviousId(): TraceId = decreaseLevel()

    fun isFirstLevel(): Boolean = level == 0
}
