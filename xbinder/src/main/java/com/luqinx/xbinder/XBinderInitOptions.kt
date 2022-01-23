package com.luqinx.xbinder

import com.luqinx.xbinder.serialize.AdapterManager
import com.luqinx.xbinder.serialize.ParcelAdapter
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

    var exceptionHandler: XBinderExceptionHandler = XBinderExceptionHandler.SimpleHandler

    var debuggable: Boolean = false

    var invokeThreshold = INVOKE_THRESHOLD_DISABLE

    var classLoader: ClassLoader = javaClass.classLoader!!

    val binderDeathHandler: BinderDeathHandler? = BinderDeathHandler.IGNORE

    var logger: ILogger = ILogger.SimpleLogger

    fun registerServiceFinder(serviceFinder: IServiceFinder) {
        if (!serviceFinders.contains(serviceFinder)) {
            serviceFinders.add(serviceFinder)
        }
    }

    fun registerTypeAdapter(type: Class<*>, adapter: ParcelAdapter<*>) {
        if (!AdapterManager.isInWhitList(type)) {
            AdapterManager.register(type, adapter)
        }
    }

}

