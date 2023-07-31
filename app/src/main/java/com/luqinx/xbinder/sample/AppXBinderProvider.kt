package com.luqinx.xbinder.sample

import android.content.Context
import com.luqinx.xbinder.*
import com.luqinx.xbinder.sample.invoketype.LocalFirstService2
import com.luqinx.xbinder.sample.invoketype.impl.LocalFirstService2Impl
import com.luqinx.xbinder.sample.invoketype.impl.LocalFirstServiceImpl
import java.lang.IllegalArgumentException

/**
 * @author  qinchao
 *
 * @since 2022/1/3
 */
class AppXBinderProvider: XBinderProvider() {

    override fun onInitOptions(context: Context?): XBinderInitOptions {
        val options = XBinderInitOptions()
        options.debuggable = true
        options.invokeThreshold = XBinderInitOptions.INVOKE_THRESHOLD_FORCE_ENABLE
        options.logger = ILogger.SimpleLogger

        options.registerServiceFinder(object : IServiceFinder {
            override fun doFind(
                fromProcess: String,
                clazz: Class<*>,
                consTypes: Array<*>?,
                constArgs: Array<*>?
            ): ILightBinder? {
                return when(clazz) {
                    LocalFirstServiceImpl::class.java -> LocalFirstServiceImpl()
                    LocalFirstService2Impl::class.java -> LocalFirstService2Impl()
                    else -> null
                }
            }
        })


        return options
    }

    override fun avoidReflectionClasses(): List<Class<*>>? {
        return null
    }
}