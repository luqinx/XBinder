package com.luqinx.xbinder

import com.luqinx.interceptor.Interceptor
import com.luqinx.interceptor.OnInvoke
import com.luqinx.xbinder.annotation.InvokeType
import java.lang.reflect.Method

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal object ServiceProxyFactory {

    fun <T : IBinderService> newServiceProxy(options: NewServiceOptions<T>): T {
        val service = newProxy(options)
        service.`_$newConstructor_`(options.constructorTypes, options.constructorArgs)
        return service
    }

    fun <T : IBinderService> newCallbackProxy(options: NewCallbackOptions<T>): T {
        val callback = newProxy(options)
//        callback.`_$bindCallbackProxy_`(options.instanceId)
        return callback
    }

    private fun <T : IBinderService> newProxy(options: NewServiceOptions<T>): T {
        return options.run {
            Interceptor.of(null as T?).interfaces(serviceClass).intercepted(true)
                .invoke(object : OnInvoke<T?> {
                    private val objectBehaviors: ObjectBehaviors =
                        ObjectBehaviors(
                            serviceClass,
                            options.processName,
                            if (options is NewCallbackOptions) options.instanceId else null
                        )

                    private val LOCAL_BEHAVIORS: ProxyBehaviors by lazy {
                        ProxyBehaviors(processName, serviceClass, objectBehaviors.hashCode())
                    }

                    override fun onInvoke(source: T?, method: Method?, args: Array<Any?>?): Any? {
//                        Refined.start()
//                        logger.d(message = "onInvoke: ${method?.name}(${args?.contentDeepToString()})")
//                        Refined.finish("logger onInvoke ")

                        return method?.apply {
                            val remoteCaller: Any = when (declaringClass) {
                                Any::class.java -> objectBehaviors
                                IProxyBehaviors::class.java -> LOCAL_BEHAVIORS
                                else -> BinderBehaviors
                            }

                            fun localInvoker(): T? = ServiceProvider.doFind(
                                options.processName,
                                objectBehaviors.hashCode(),
                                options.serviceClass,
                                options.constructorTypes,
                                options.constructorArgs
                            ) as T?

                            var caller = when (invokeType) {
                                InvokeType.LOCAL_ONLY -> localInvoker()
                                InvokeType.LOCAL_FIRST -> localInvoker() ?: remoteCaller
                                InvokeType.REMOTE_ONLY -> remoteCaller
                                InvokeType.REMOTE_FIRST -> remoteCaller
                                else -> {
                                    throw IllegalArgumentException("unknown invoke type $invokeType")
                                }
                            } ?: options.defService ?: noOpService(options.serviceClass)

                            if (caller == BinderBehaviors) {
                                try {
                                    return BinderBehaviors.invokeMethod(
                                        serviceClass, //
                                        method,
                                        args,
                                        options,
                                        objectBehaviors.hashCode()
                                    )
                                } catch (e: XBinderException) {
                                    if (e.code != BinderInvoker.ERROR_CODE_REMOTE_NOT_FOUND) {
                                        caller =
                                            localInvoker() ?: options.defService ?: noOpService(
                                                options.serviceClass
                                            )
                                    } else {
                                        throw e
                                    }
                                }
                            }
                            args?.let {
                                return invoke(caller, *it)
                            } ?: run {
                                return invoke(caller)
                            }
                        }
                    }

                }).newInstance()!!
        }
    }
}