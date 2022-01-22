package com.luqinx.xbinder

interface IServiceFinder {
    fun doFind(clazz: Class<*>, consTypes: Array<out Class<*>>?, constArgs: Array<*>?): IBinderService?
}