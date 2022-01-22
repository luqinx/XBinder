package com.luqinx.xbinder

import android.util.Log
import com.luqinx.xbinder.XBinder.initOptions

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
interface ILogger {
    fun v(tag: String = SimpleLogger.TAG, message: String)

    fun i(tag: String = SimpleLogger.TAG, message: String)

    fun d(tag: String = SimpleLogger.TAG, message: String)

    fun w(tag: String = SimpleLogger.TAG, message: String)

    fun e(tag: String = SimpleLogger.TAG, message: String, e: Throwable? = null)

    object SimpleLogger: ILogger {
        const val TAG = "xbinder"

        override fun v(tag: String, message: String) {
            Log.v(tag, message)
        }

        override fun i(tag: String, message: String) {
            Log.i(tag, message)
        }

        override fun d(tag: String, message: String) {
            if (initOptions.debuggable) {
                Log.d(tag, message)
            }
        }

        override fun w(tag: String, message: String) {
            Log.w(tag, message)
        }

        override fun e(tag: String, message: String, e: Throwable?) {
            Log.e(tag, message, e)
        }

    }
}