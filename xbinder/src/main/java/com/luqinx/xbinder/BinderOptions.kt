package com.luqinx.xbinder

import com.luqinx.xbinder.annotation.InvokeType

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal class BinderOptions<T: IBinderService>(val serviceClass: Class<T>, val processName: String) {

    var constructorTypes: Array<Class<*>>? = null

    var constructorArgs: Array<*>? = null

    var defService: T? = null

    var isCallback: Boolean = false

    @InvokeType var invokeType: Int = InvokeType.REMOTE_ONLY

    fun constructorTypes(consTypes: Array<Class<*>>?): BinderOptions<T> {
        this.constructorTypes = consTypes
        return this
    }

    fun constructorArgs(consArgs: Array<*>?): BinderOptions<T> {
        this.constructorArgs = consArgs
        return this
    }

    fun defaultService(defService: T?): BinderOptions<T> {
        this.defService = defService
        return this
    }

    fun setIsCallback(callback: Boolean): BinderOptions<T> {
        this.isCallback = callback
        return this
    }

    fun invokeType(invokeType: Int): BinderOptions<T> {
        this.invokeType = invokeType
        return this
    }


}