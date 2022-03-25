package com.luqinx.xbinder.misc

import android.os.SystemClock
import com.luqinx.xbinder.logger

/**
 * @author  qinchao
 *
 * @since 2022/2/20
 */
object Refined {

    private var startTime = 0L

    private const val enabled = true

    fun start() {
        if (!enabled) return
        reset()
        startTime = SystemClock.elapsedRealtimeNanos()
    }

    fun finish(tag: String) {
        if (!enabled) return
        logger.i(message = "$tag: spent ${SystemClock.elapsedRealtimeNanos() - startTime}ns")
    }

    fun log(tag: String) {
        if (!enabled) return
        logger.i(message = "$tag: spent ${SystemClock.elapsedRealtimeNanos() - startTime}ns")
        startTime = SystemClock.elapsedRealtimeNanos()
    }

    fun reset() {
        startTime = 0L
    }
}