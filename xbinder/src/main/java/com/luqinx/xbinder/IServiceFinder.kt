package com.luqinx.xbinder

interface IServiceFinder {
    fun doFind(
        fromProcess: String,
        clazz: Class<*>,
        consTypes: Array<*>?,
        constArgs: Array<*>?
    ): IBinderService?
}