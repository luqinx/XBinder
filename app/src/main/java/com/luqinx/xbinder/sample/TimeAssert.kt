package com.luqinx.xbinder.sample

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper

/**
 * @author  qinchao
 *
 * @since 2022/3/25
 */
class TimeAssert {

    companion object : HandlerThread("xbinder-sample-watchdog") {
        @Volatile
        private var started = false

        private lateinit var handler: Handler

        override fun onLooperPrepared() {
            started = true
            handler = Handler(Looper.myLooper()!!)
        }

        private fun checkStarted() {
            if (!started) throw IllegalStateException("watchdog not ready")
        }

        fun assert(value: Boolean) {
            assert(value) { "Assertion failed" }
        }

        /**
         * Throws an [AssertionError] calculated by [lazyMessage] if the [value] is false
         * and runtime assertions have been enabled on the JVM using the *-ea* JVM option.
         */
        private inline fun assert(value: Boolean, lazyMessage: () -> Any) {
            if (!value) {
                val message = lazyMessage()
                throw AssertionError(message)
            }
        }

        fun startCountDown(
            _countDown: Int = -1,
            message: String = "Assertion failed",
            timeout: Long = 500
        ): TimeAssert {
            val assert = TimeAssert()
            assert.startCountDown(_countDown, message, timeout)
            return assert
        }
    }

    @Volatile
    private var countDown: Int = -1

    private fun startCountDown(_countDown: Int = -1, message: String = "", timeout: Long = 500) {
        this.countDown = _countDown
        checkStarted()
        handler.postDelayed({
            if (countDown > 0) {
                throw AssertionError("$message , timeout $timeout, countDown = $countDown")
            }
            countDown = -1
        }, timeout)
    }

    fun countDown() {
        countDown--
        if (countDown == 0) {
            println("case verify ok")
        } else if (countDown < 0) {
            throw IllegalStateException("count down overflow")
        }
    }

}