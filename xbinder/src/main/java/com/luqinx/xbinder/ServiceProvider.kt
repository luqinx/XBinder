package com.luqinx.xbinder

import android.util.SparseArray

internal object ServiceProvider {

    private val serviceImplCache = hashMapOf<String, SparseArray<IBinderService>>()

    private val instanceCache = hashMapOf<String, IBinderService>()

    fun doFind(
        fromProcess: String,
        delegateId: Int,
        clazz: Class<*>,
        consTypes: Array<*>?,
        constArgs: Array<*>?
    ): IBinderService? {
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
                    remoteService = it.doFind(fromProcess, clazz, consTypes, constArgs)
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

    fun registerServiceInstance(instanceId: String, instance: IBinderService) {
        synchronized(instanceCache) {
            instanceCache[instanceId] = instance
        }
    }

    fun unregisterServiceInstance(instanceId: String) {
        synchronized(instanceCache) {
            instanceCache.remove(instanceId)
        }
    }

    fun getServiceInstance(instanceId: String): IBinderService? {
        synchronized(instanceCache) {
            return instanceCache[instanceId]
        }
    }

    fun getServiceImpl(fromProcess: String, delegateId: Int): IBinderService? {
        return serviceImplCache[fromProcess]?.get(delegateId)
    }

    fun onBinderDeath(process: String) {
        synchronized(serviceImplCache) {
            serviceImplCache.remove(process)
        }
    }
}