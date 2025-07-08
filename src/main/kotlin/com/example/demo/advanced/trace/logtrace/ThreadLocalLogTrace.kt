package com.example.demo.advanced.trace.logtrace

import com.example.demo.advanced.trace.logtrace.models.TraceId
import com.example.demo.advanced.trace.logtrace.models.TraceStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ThreadLocalLogTrace : LogTrace {
    companion object {
        private const val START_PREFIX = "-->"
        private const val COMPLETE_PREFIX = "<--"
        private const val EX_PREFIX = "<X-"
    }

    private val log: Logger = LoggerFactory.getLogger(ThreadLocalLogTrace::class.java)

    private val traceIdHolder: ThreadLocal<TraceId> = ThreadLocal<TraceId>()

    override fun begin(message: String): TraceStatus {
        syncTraceId()
        val traceId: TraceId = traceIdHolder.get()
        val startTimeMs = System.currentTimeMillis()
        log.info("[{}] {}{}", traceId.id, addSpace(START_PREFIX, traceId.level), message)
        return TraceStatus(traceId, startTimeMs, message)
    }

    private fun syncTraceId() {
        val traceId: TraceId? = traceIdHolder.get()
        if (traceId == null) {
            traceIdHolder.set(TraceId())
        } else {
            traceIdHolder.set(traceId.createNextId())
        }
    }

    override fun end(status: TraceStatus) {
        complete(status, null)
    }

    override fun exception(status: TraceStatus?, e: Exception) {
        complete(status, e)
    }

    private fun complete(status: TraceStatus?, e: Exception?) {
        if (status == null) return

        val stopTimeMs = System.currentTimeMillis()
        val resultTimeMs: Long = stopTimeMs - status.startTimeMs
        val traceId: TraceId = status.traceId
        if (e == null) {
            log.info(
                "[{}] {}{} time={}ms",
                traceId.id,
                addSpace(COMPLETE_PREFIX, traceId.level),
                status.message,
                resultTimeMs
            )
        } else {
            log.info(
                "[{}] {}{} time={}ms ex={}",
                traceId.id,
                addSpace(EX_PREFIX, traceId.level),
                status.message,
                resultTimeMs,
                e.toString()
            )
        }

        releaseTraceId()
    }

    private fun releaseTraceId() {
        val traceId: TraceId = traceIdHolder.get()
        if (traceId.isFirstLevel()) {
            traceIdHolder.remove() //destroy
        } else {
            traceIdHolder.set(traceId.createPreviousId())
        }
    }

    private fun addSpace(prefix: String?, level: Int): String {
        val sb = StringBuilder()
        for (i in 0..<level) {
            sb.append(if (i == level - 1) "|" + prefix else "|   ")
        }
        return sb.toString()
    }
}
