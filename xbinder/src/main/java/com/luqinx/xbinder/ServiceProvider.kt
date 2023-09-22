package com.luqinx.xbinder

import android.util.SparseArray
import com.luqinx.interceptor.Interceptor

internal object ServiceProvider {

    private val serviceImplCache = hashMapOf<String, HashMap<String, ILightBinder>>()

    private val instanceCache = hashMapOf<String, HashMap<String,ILightBinder>>()

    fun doFind(
        fromProcess: String,
        delegateId: String,
        clazz: Class<*>,
        consTypes: Array<*>?,
        constArgs: Array<*>?
    ): Any? {
        var remoteService = serviceImplCache[fromProcess]?.get(delegateId)
        if (remoteService != null) {
            return remoteService
        }

        synchronized(serviceImplCache) {
            // double check
            remoteService = serviceImplCache[fromProcess]?.get(delegateId)
            if (remoteService == null) {
                serviceFinders.forEach {
                    logger.d(message  = "find $clazz impl by delegateId $delegateId")
                    val delegate = it.doFind(fromProcess, clazz, consTypes, constArgs)
                    remoteService = when {
                        delegate is ILightBinder -> {
                            delegate
                        }
                        delegate != null -> {
                            Interceptor.of(delegate).interfaces(ILightBinder::class.java)
                                .newInstance() as ILightBinder
                        }
                        else -> {
                            null
                        }
                    }
                    val service = remoteService ?: return@forEach
                    if (serviceImplCache[fromProcess] == null) {
                        serviceImplCache[fromProcess] = HashMap()
                    }
                    serviceImplCache[fromProcess]!!.put(delegateId, service)
                    return@forEach
                }
            }
            return remoteService
        }
    }

    fun registerServiceInstance(fromProcess: String, instanceId: String, instance: ILightBinder) {
        synchronized(instanceCache) {
            instanceCache[fromProcess]?.apply {
                this[instanceId] = instance
            } ?: run {
                 instanceCache[fromProcess] = hashMapOf(
                     instanceId to instance
                 )
            }
        }
    }

    fun unregisterServiceInstance(fromProcess: String, instanceId: String) {

        synchronized(instanceCache) {
            instanceCache[fromProcess]?.remove(instanceId)
        }
        logger.d(message = "unregisterServiceInstance(${fromProcess}): $instanceId ")
    }

    fun getServiceInstance(fromProcess: String, instanceId: String): ILightBinder? {
        synchronized(instanceCache) {
            return instanceCache[fromProcess]?.get(instanceId)
        }
    }

    fun getServiceImpl(fromProcess: String, delegateId: String): ILightBinder? {
        return serviceImplCache[fromProcess]?.get(delegateId)
    }

    fun onBinderDeath(process: String) {
        synchronized(serviceImplCache) {
            serviceImplCache.remove(process)
        }
        synchronized(instanceCache) {
            instanceCache.remove(process)
        }
    }
}