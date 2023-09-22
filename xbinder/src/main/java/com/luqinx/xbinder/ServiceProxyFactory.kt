package com.luqinx.xbinder

import com.luqinx.interceptor.Interceptor
import com.luqinx.interceptor.OnInvoke
import com.luqinx.xbinder.annotation.InvokeType
import com.luqinx.xbinder.misc.Refined
import java.lang.reflect.Method

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal object ServiceProxyFactory {

    fun <T> newServiceProxy(options: NewServiceOptions<T>): T {
        Refined.start()
        val service = newProxy(options)
        Refined.log("new proxy")
        (service as ILightBinder).setInstanceId(
            service.`_$newConstructor_`(options.constructorTypes, options.constructorArgs)
        )
        Refined.log("_\$newConstructor_")
        return service
    }

    fun <T> newCallbackProxy(options: NewServiceOptions<T>): T {
        options.isCallback = true
        options.invokeType = InvokeType.REMOTE_ONLY
        //        callback.`_$bindCallbackProxy_`(options.instanceId)
        return newProxy(options)
    }

    private fun <T> newProxy(options: NewServiceOptions<T>): T {
        return options.run {
            val interfaces =
                if (!ILightBinder::class.java.isAssignableFrom(serviceClass)) {
                    arrayOf(ILightBinder::class.java, serviceClass, IProxyFlag::class.java)
                } else {
                    arrayOf(serviceClass, IProxyFlag::class.java)
                }
            Interceptor.of(null as T?).interfaces(*interfaces)
                .intercepted(true)
                .invoke(object : OnInvoke<T?> {
                    private val objectBehaviors: ObjectBehaviors =
                        ObjectBehaviors(
                            serviceClass,
                            options.processName,
                            options.instanceId
                        )

                    private val LOCAL_BEHAVIORS: ProxyBehaviors by lazy {
                        ProxyBehaviors(
                            processName,
                            serviceClass,
                            objectBehaviors.uuid,
                            options.instanceId
                        )
                    }

                    override fun onInvoke(source: T?, method: Method, args: Array<Any?>?): Any? {
                        Refined.start()
                        logger.d(message = "onInvoke: ${method.name}(${args?.contentDeepToString()})")
                        Refined.finish("logger onInvoke ")

                        return method.apply {
                            val remoteCaller: Any = when (declaringClass) {
                                Any::class.java -> objectBehaviors
                                IProxyBehaviors::class.java -> LOCAL_BEHAVIORS
                                else -> BinderBehaviors
                            }

                            var caller = when (invokeType) {
                                InvokeType.REMOTE_ONLY -> remoteCaller
                                InvokeType.REMOTE_FIRST -> remoteCaller
                                else -> {
                                    throw IllegalArgumentException("unknown invoke type $invokeType")
                                }
                            }

                            if (caller == BinderBehaviors) {
                                try {
                                    return BinderBehaviors.invokeMethod(
                                        serviceClass, //
                                        method,
                                        args,
                                        options,
                                        objectBehaviors.uuid
                                    )
                                } catch (e: XBinderException) {
                                    if (e.code != BinderInvoker.ERROR_CODE_REMOTE_NOT_FOUND) {
                                        val localInvoker: T? = ServiceProvider.doFind(
                                            options.processName,
                                            objectBehaviors.uuid,
                                            options.serviceClass,
                                            options.constructorTypes,
                                            options.constructorArgs
                                        ) as T?
                                        caller =
                                            localInvoker ?: options.defService ?: noOpService(
                                                options.serviceClass
                                            )
                                    } else {
                                        throw e
                                    }
                                }
                            } else if (declaringClass == IProxyBehaviors::class.java && (caller !is IProxyBehaviors)) {
                                return null
                            }
                            logger.d(message = "caller is $caller, is instance of IProxyBehaviors? ${caller is IProxyBehaviors}")
                            logger.d(message = "$this declaringClass is $declaringClass");

                            return if (args != null) {
                                method.invoke(caller, *args)
                            } else method.invoke(caller)

                        }
                    }

                }).newInstance()!!
        }
    }
}