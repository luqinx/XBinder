package com.luqinx.xbinder

import android.content.Context
import com.luqinx.xbinder.XBinderInitOptions.Companion.INVOKE_THRESHOLD_DISABLE
import com.luqinx.xbinder.serialize.toClass
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy
import java.util.*

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */

internal const val CORE_METHOD_NEW_CONSTRUCTOR = "_\$newConstructor_"

internal const val CORE_METHOD_BIND_CALLBACK_PROXY = "_\$bindCallbackProxy_"

internal const val CORE_METHOD_UNREGISTER_INSTANCE = "_\$unregisterInstance_"



internal lateinit var context: Context

internal val thisProcess by lazy { XBinderProvider.processName }

internal var invokeThreshold: Long = INVOKE_THRESHOLD_DISABLE

internal var binderDeathHandler: BinderDeathHandler = BinderDeathHandler.IGNORE

internal var logger: ILogger = ILogger.SimpleLogger

internal var classloader = XBinder::class.java.classLoader

internal val interactiveProcessMap = hashMapOf<String, List<String>>()

internal var serviceFinders: ArrayList<IServiceFinder> = arrayListOf()

internal var exceptionHandler: XBinderExceptionHandler = XBinderExceptionHandler.SimpleHandler

internal var debuggable = false

private val noOpHandler = InvocationHandler { _, _, _ -> null }

internal fun noOpService(serviceClass: Class<*>): Any =
    Proxy.newProxyInstance(classloader, arrayOf(serviceClass), noOpHandler)