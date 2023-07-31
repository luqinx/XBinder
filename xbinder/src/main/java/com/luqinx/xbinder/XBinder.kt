package com.luqinx.xbinder

import com.luqinx.xbinder.annotation.InvokeType
import com.luqinx.xbinder.serialize.AdapterManager
import com.luqinx.xbinder.serialize.ParcelAdapter
import java.lang.reflect.Type

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
object XBinder {

    var xbinderReady = false

    @JvmStatic
    @JvmOverloads
    fun <T> getService(
        serviceClass: Class<T>,
        processName: String = "",
        constructorTypes: Array<Class<*>>? = null,
        constructorArgs: Array<*>? = null,
        defService: T? = null,
        @InvokeType invokeType_: Int = InvokeType.REMOTE_ONLY
    ): T? {
        return getServiceInner(
            serviceClass,
            processName,
            constructorTypes as Array<*>?,
            constructorArgs,
            defService,
            invokeType_
        )
    }

    /**
     *  @see getServiceInner
     */
    @JvmStatic
    @JvmOverloads
    fun <T : ILightBinder> getParameterizedService(
        serviceClass: Class<T>,
        processName: String = "",
        constructorTypes: Array<Type>? = null,
        constructorArgs: Array<*>? = null,
        defService: T? = null,
        @InvokeType invokeType_: Int = InvokeType.REMOTE_ONLY
    ): T? {
        // not complete
        return getServiceInner(
            serviceClass,
            processName,
            constructorTypes as Array<*>?,
            constructorArgs,
            defService,
            invokeType_
        )
    }

    /**
     *  this method will return a dynamic proxy of the given service class who will calling the remote methods
     *
     *  @param serviceClass the remote service's class
     *  @param processName remote process name
     *  @param constructorTypes the remote service constructors' types
     *  @param constructorArgs the remote service constructors' values, it must be null if the constructorTypes is null
     *  and it's array size must be equals with constructorTypes
     *  @param defService default service will be called if the remote service not exists
     *  @param invokeType_
     */
    private fun <T> getServiceInner(
        serviceClass: Class<T>,
        processName: String = "",
        constructorTypes: Array<*>? = null,
        constructorArgs: Array<*>? = null,
        defService: T? = null,
        @InvokeType invokeType_: Int = InvokeType.REMOTE_ONLY
    ): T? {
        if (constructorArgs != null && constructorTypes != null && constructorArgs.size != constructorTypes.size) {
            throw IllegalArgumentException("constructor arguments' size not match: args{${constructorArgs}}, types{${constructorTypes}}")
        }
        if ((constructorArgs == null && constructorTypes != null) || (constructorArgs != null && constructorTypes == null)) {
            throw IllegalArgumentException("constructor arguments not match: arguments and it's types should be null or not null at the same time")
        }
        if (!xbinderReady) {
            throw IllegalStateException("xbinder config error: please refer to the readme.md file for xbinder's initialization.")
        }

        // realProcessName spent 0.2ms in xiaomi-9A
        val realProcessName: String = when {
            processName.isEmpty() -> {
                currentProcessName()
            }
            processName.startsWith(":") -> {
                currentProcessName() + processName.replaceFirst(":", ".")
            }
            else -> {
                processName.replaceFirst(":", ".")
            }
        }
        val invokeType = if (realProcessName == currentProcessName()) {
            if (invokeType_ == InvokeType.REMOTE_ONLY) {
                throw java.lang.IllegalArgumentException("check your process name please")
            }
            InvokeType.LOCAL_ONLY
        } else {
            invokeType_
        }

        val localCaller =
            if (invokeType == InvokeType.LOCAL_ONLY || invokeType == InvokeType.LOCAL_FIRST) {
                ServiceProvider.doFind(
                    processName,
                    0,
                    serviceClass,
                    constructorTypes,
                    constructorArgs
                ) as T? ?: defService
            } else null

        if (invokeType == InvokeType.LOCAL_ONLY || localCaller != null) {
            return localCaller
        }

        return ServiceProxyFactory.newServiceProxy(
            NewServiceOptions(serviceClass, realProcessName)
                .constructorTypes(constructorTypes)
                .constructorArgs(constructorArgs)
                .invokeType(invokeType)
                .defaultService(defService)
        )
    }

    @JvmStatic
    fun registerTypeAdapter(type: Class<*>, adapter: ParcelAdapter<*>) {
        if (!AdapterManager.isInWhitList(type)) {
            AdapterManager.register(type, adapter)
        }
    }

    /**
     * notify the processes who communication with this process has been ready
     *
     * it's useful for telling the relative processes when they can communication with me,
     * if not, they don't known when my initialization finish.
     */
    fun notifyProcessReady() {
        //todo
    }

    @JvmStatic
    fun addServiceFinder(serviceFinder: IServiceFinder) {
        serviceFinders.add(serviceFinder)
    }

    @JvmStatic
    fun currentProcessName(): String {
        return XBinderProvider.processName
    }

    internal fun hasGradlePlugin(): Boolean {
        return false
    }

}