package com.luqinx.xbinder

import com.luqinx.interceptor.Interceptor
import com.luqinx.interceptor.OnInvoke
import java.lang.reflect.Method

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal object BinderFactory {

    fun <T: IBinderService> newBinder(options: BinderOptions<T>): T {
        val service = options.run {
            Interceptor.of(null as T?).interfaces(serviceClass).intercepted(true)
                .invoke(object : OnInvoke<T?> {
                    private val objectBehaviors: ObjectBehaviors = ObjectBehaviors(serviceClass)

                    private val LOCAL_BEHAVIORS: ProxyBehaviors by lazy {
                        ProxyBehaviors(processName, serviceClass, objectBehaviors.hashCode())
                    }
                    override fun onInvoke(source: T?, method: Method?, args: Array<Any?>?): Any? {
                        logger.d(message = "onInvoke: ${method?.name}(${args?.contentDeepToString()})")
                        method?.apply {
                            return when (declaringClass) {
                                Any::class.java -> {
                                    args?.let {
                                        invoke(objectBehaviors, *it)
                                    } ?: run {
                                        invoke(objectBehaviors)
                                    }
                                }
                                IProxyBehaviors::class.java -> {
                                    args?.let {
                                        invoke(LOCAL_BEHAVIORS, *it)
                                    } ?: run {
                                        invoke(LOCAL_BEHAVIORS)
                                    }
                                }
                                else -> {
                                    BinderBehaviors.invokeMethod(serviceClass, method, args, options, objectBehaviors.hashCode())
                                }
                            }
                        }
                        return null
                    }

                }).newInstance()!!
        }
        service.`_$newConstructor_`(options.constructorTypes, options.constructorArgs)
        return service
    }

    fun <T: IBinderService> newCallback(options: BinderOptions<T>): T? {
        return null
    }
}