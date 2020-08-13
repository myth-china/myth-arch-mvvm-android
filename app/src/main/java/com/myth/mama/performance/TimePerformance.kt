package com.myth.mama.performance

import android.util.Log

/**
 * 时间性能统计类
 */
object TimePerformance {
    fun getTimeRecorder(name: String): TimeRecorder {
        return TimeRecorder(name)
    }
}

class TimeRecorder(private val name: String) {

    private var startMillis = 0L
    private var startNano = 0L

    fun start() {
        startMillis = System.currentTimeMillis()
        startNano = System.nanoTime()
    }

    fun print() {
        Log.d(
            name,
            "consume time >>>> ${System.currentTimeMillis() - startMillis}ms [${System.nanoTime() - startNano}ns]"
        )
    }
}