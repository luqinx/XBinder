package com.luqinx.xbinder

import android.content.Context
import com.luqinx.xbinder.serialize.AdapterManager
import com.luqinx.xbinder.keepalive.KeepAliveStrategy
import com.luqinx.xbinder.serialize.ParcelAdapter

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
object XBinder {

    internal lateinit var context: Context

    internal lateinit var initOptions: XBinderInitOptions

    @JvmStatic
    @JvmOverloads
    fun init(context: Context, initOptions: XBinderInitOptions? = null) {
        this.context = context
        this.initOptions = initOptions ?: XBinderInitOptions()
        this.initOptions.keepAliveStrategyHandler?.apply {
            keepAliveStrategy = KeepAliveStrategy.CUSTOM
            keepAliveStrategy.setCustomAliveStrategyHandler(this)
        }
    }

    /**
     *  this method will return a dynamic proxy of the given service class who will calling the remote methods
     *
     *  @param binderClass the remote service's class
     *  @param processName remote process name
     *  @param constructorTypes the remote service constructors' types
     *  @param constructorArgs the remote service constructors' values, it must be null if the constructorTypes is null
     *  and it's array size must be equals with constructorTypes
     *  @param defService default service will be called if the remote service not exists
     */
    @JvmStatic
    @JvmOverloads
    fun <T: IBinderService> getBinder(binderClass: Class<T>, processName: String, constructorTypes: Array<Class<*>>? = null, constructorArgs: Array<*>? = null, defService: T? = null): T {
        if (constructorArgs != null && constructorTypes != null && constructorArgs.size != constructorTypes.size) {
            throw IllegalArgumentException("constructor arguments' size not match: args{${constructorArgs}}, types{${constructorTypes}}")
        }
        if ((constructorArgs == null && constructorTypes != null) || (constructorArgs != null && constructorTypes == null)) {
            throw IllegalArgumentException("constructor arguments not match: arguments and it's types should be null or not null at the same time")
        }
        return BinderFactory.newBinder(
            BinderOptions(binderClass, processName)
            .constructorTypes(constructorTypes)
            .constructorArgs(constructorArgs)
            .defaultService(defService)
        )
    }

    @JvmStatic
    fun registerTypeAdapter(type: Class<*>, adapter: ParcelAdapter<*>) {
        AdapterManager.register(type, adapter)
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
        return BinderContentProvider.processName
    }
}