package com.luqinx.xbinder.sample

import android.content.Context
import com.luqinx.xbinder.*
import com.luqinx.xbinder.sample.simple.PrimitiveArrayTypeService
import com.luqinx.xbinder.sample.simple.PrimitiveArrayTypeServiceImpl
import com.luqinx.xbinder.sample.simple.PrimitiveTypeService
import com.luqinx.xbinder.sample.simple.PrimitiveTypeServiceImpl

/**
 * @author  qinchao
 *
 * @since 2022/1/3
 */
class RemoteXBinderProvider: XBinderProvider() {

    override fun onInitOptions(context: Context?): XBinderInitOptions {
        val options = XBinderInitOptions()
        options.debuggable = true
        options.invokeThreshold = XBinderInitOptions.INVOKE_THRESHOLD_FORCE_ENABLE
        options.logger = ILogger.SimpleLogger

        options.registerServiceFinder(object : IServiceFinder{
            override fun doFind(
                fromProcess: String,
                clazz: Class<*>,
                consTypes: Array<out Class<*>>?,
                constArgs: Array<*>?
            ): IBinderService? {
                if (clazz == PrimitiveTypeService::class.java) {
                    return PrimitiveTypeServiceImpl()
                } else if (clazz == PrimitiveArrayTypeService::class.java) {
                    return PrimitiveArrayTypeServiceImpl()
                }
                return null
            }
        })


        return options
    }
}