package com.luqinx.xbinder

import com.luqinx.xbinder.keepalive.KeepAliveStrategy

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
class XBinderInitOptions {

    companion object {
        const val INVOKE_THRESHOLD_DISABLE = -1L
        const val INVOKE_THRESHOLD_FORCE_ENABLE = -2L
    }

    var exceptionHandler: XBinderExceptionHandler? = XBinderExceptionHandler.SimpleHandler

    var debuggable: Boolean = false

    var invokeThreshold = INVOKE_THRESHOLD_DISABLE

    var classLoader: ClassLoader? = XBinder::class.java.classLoader

    var keepAliveStrategyHandler: KeepAliveStrategy.AliveStrategyHandler? = null

    var logger: ILogger? = ILogger.SimpleLogger

}