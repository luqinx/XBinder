package com.luqinx.xbinder

interface IServiceFinder {
    fun doFind(
        fromProcess: String,
        clazz: Class<*>,
        consTypes: Array<out Class<*>>?,
        constArgs: Array<*>?
    ): IBinderService?
}