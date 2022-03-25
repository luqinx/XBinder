package com.luqinx.xbinder

import com.luqinx.xbinder.annotation.InvokeType

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal open class NewServiceOptions<T>(
    val serviceClass: Class<T>, val processName: String, var instanceId: String? = null
) {

    var constructorTypes: Array<*>? = null

    var constructorArgs: Array<*>? = null

    var defService: T? = null

    var isCallback: Boolean = false


    @InvokeType
    var invokeType: Int = InvokeType.REMOTE_ONLY

    fun constructorTypes(consTypes: Array<*>?): NewServiceOptions<T> {
        this.constructorTypes = consTypes
        return this
    }

    fun constructorArgs(consArgs: Array<*>?): NewServiceOptions<T> {
        this.constructorArgs = consArgs
        return this
    }

    fun defaultService(defService: T?): NewServiceOptions<T> {
        this.defService = defService
        return this
    }

    fun setIsCallback(callback: Boolean): NewServiceOptions<T> {
        this.isCallback = callback
        return this
    }

    fun invokeType(invokeType: Int): NewServiceOptions<T> {
        this.invokeType = invokeType
        return this
    }


}