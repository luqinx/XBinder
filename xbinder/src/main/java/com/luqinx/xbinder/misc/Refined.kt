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

    private const val enabled = false

    fun start() {
        if (!enabled) return
        reset()
        startTime = SystemClock.elapsedRealtimeNanos()
    }

    fun finish(tag: String) {
        if (!enabled) return
        logger.i("XBinder", " $tag: spent ${SystemClock.elapsedRealtimeNanos() - startTime}")
    }

    fun reset() {
        startTime = 0L
    }
}