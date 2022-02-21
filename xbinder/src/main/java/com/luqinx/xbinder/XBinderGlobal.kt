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

internal val coreService = object : BinderChannel.Stub() {
    override fun invokeMethod(rpcArgument: ChannelMethodArgument): ChannelMethodResult {
        logger.d(
            message = "invokeMethod by ${rpcArgument.delegateId}: ${rpcArgument.returnType} ${rpcArgument.method}(${
                rpcArgument.args?.let {
                    return@let it.contentDeepToString()
                } ?: ""
            })")
        val result = ChannelMethodResult()
        result.succeed = true
        val clazzImpl: IBinderService?
        if (rpcArgument.method == CORE_METHOD_NEW_CONSTRUCTOR) {
            rpcArgument.run {
                val start = System.currentTimeMillis()
                clazzImpl = ServiceProvider.doFind(fromProcess, delegateId, clazz.toClass()!!, genericArgTypes, args)
                result.value = clazzImpl != null
                result.invokeConsumer = System.currentTimeMillis() - start
                return result
            }
        } else {
            rpcArgument.run {
                clazzImpl = ServiceProvider.getServiceImpl(fromProcess, delegateId)
            }
        }

        if (clazzImpl == null) {
            result.succeed = false
            result.errCode = BinderInvoker.ERROR_CODE_REMOTE_NOT_FOUND
            result.errMessage = "not found the implementation of ${rpcArgument.clazz.toClass()}."
            return result
        }
        try {
            if (rpcArgument.argTypes == null || rpcArgument.argTypes!!.isEmpty()) {
                val method = clazzImpl.javaClass.getDeclaredMethod(rpcArgument.method)
                method.isAccessible = true
                val start = System.currentTimeMillis()
                result.value = method.invoke(clazzImpl)
                result.invokeConsumer = System.currentTimeMillis() - start
            } else {
                val method = clazzImpl.javaClass.getDeclaredMethod(rpcArgument.method, *rpcArgument.argTypes!!)
                method.isAccessible = true
                val start = System.currentTimeMillis()
                result.value = method.invoke(clazzImpl, *rpcArgument.args!!)
                result.invokeConsumer = System.currentTimeMillis() - start
            }
        } catch (e: Throwable) {
            result.succeed = false
            result.errMessage = e.message
            exceptionHandler.handle(e)
        }
        return result
    }

    override fun registerCallbackChannel(channel: BinderChannel) {
        BinderChannelProvider.addBinderChannel()
    }

    override fun unRegisterCallbackMethod(fromProcess: String, methodId: String) {
//            ServiceStore.unregisterMethodCallback(fromProcess, methodId)
    }
}