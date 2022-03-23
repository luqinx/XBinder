package com.luqinx.xbinder

import android.util.SparseArray
import com.luqinx.interceptor.Interceptor

internal object ServiceProvider {

    private val serviceImplCache = hashMapOf<String, SparseArray<ILightBinder>>()

    private val instanceCache = hashMapOf<String, HashMap<String,ILightBinder>>()

    fun doFind(
        fromProcess: String,
        delegateId: Int,
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
                    if (remoteService != null) {
                        if (serviceImplCache[fromProcess] == null) {
                            serviceImplCache[fromProcess] = SparseArray()
                        }
                        serviceImplCache[fromProcess]!!.put(delegateId, remoteService)
                        return@forEach
                    }
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

    fun getServiceImpl(fromProcess: String, delegateId: Int): ILightBinder? {
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