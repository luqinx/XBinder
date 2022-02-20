package com.luqinx.xbinder.sample

import android.content.Context
import com.luqinx.xbinder.*

/**
 * @author  qinchao
 *
 * @since 2022/1/3
 */
class AppXBinderProvider: XBinderProvider() {

    override fun onInitOptions(context: Context?): XBinderInitOptions {
        val options = XBinderInitOptions()
        options.debuggable = false
        options.invokeThreshold = XBinderInitOptions.INVOKE_THRESHOLD_FORCE_ENABLE
        options.logger = ILogger.SimpleLogger

        options.registerServiceFinder(object : IServiceFinder {
            override fun doFind(
                fromProcess: String,
                clazz: Class<*>,
                consTypes: Array<*>?,
                constArgs: Array<*>?
            ): IBinderService? {
                return null
            }
        })


        return options
    }

    override fun avoidReflectionClasses(): List<Class<*>>? {
        return null
    }
}