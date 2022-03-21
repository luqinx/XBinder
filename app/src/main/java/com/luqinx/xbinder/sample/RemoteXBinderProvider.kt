package com.luqinx.xbinder.sample

import android.content.Context
import chao.android.tools.servicepool.Spa
import chao.java.tools.servicepool.IService
import com.luqinx.xbinder.*
import com.luqinx.xbinder.sample.async.AsyncCallJavaService
import com.luqinx.xbinder.sample.async.AsyncCallService
import com.luqinx.xbinder.sample.async.impl.AsyncCallJavaServiceImpl
import com.luqinx.xbinder.sample.async.impl.AsyncCallServiceImpl
import com.luqinx.xbinder.sample.callback.CallbackService
import com.luqinx.xbinder.sample.callback.CallbackServiceFragment
import com.luqinx.xbinder.sample.callback.CallbackServiceImpl
import com.luqinx.xbinder.sample.simple.*

/**
 * @author  qinchao
 *
 * @since 2022/1/3
 */
class RemoteXBinderProvider: XBinderProvider() {

    override fun onInitOptions(context: Context?): XBinderInitOptions {
        val options = XBinderInitOptions()
        options.debuggable = false
        options.invokeThreshold = XBinderInitOptions.INVOKE_THRESHOLD_FORCE_ENABLE
        options.logger = ILogger.SimpleLogger

        options.registerServiceFinder(object : IServiceFinder{
            override fun doFind(
                fromProcess: String,
                clazz: Class<*>,
                consTypes: Array<*>?,
                constArgs: Array<*>?
            ): IBinderService {
                return when (clazz) {
                    PrimitiveTypeService::class.java -> PrimitiveTypeServiceImpl()
                    PrimitiveArrayTypeService::class.java -> PrimitiveArrayTypeServiceImpl()
                    AsyncCallService::class.java -> AsyncCallServiceImpl()
                    AsyncCallJavaService::class.java -> AsyncCallJavaServiceImpl()
                    BinderTypeService::class.java -> BinderTypeServiceImpl()
                    PrimitiveBoxArrayTypeService::class.java -> PrimitiveBoxArrayTypeServiceImpl()
                    CallbackService::class.java -> CallbackServiceImpl()
                    else -> {
                        Spa.getService(clazz as Class<IService>) as IBinderService
                    }
                }
            }
        })


        return options
    }

    override fun avoidReflectionClasses(): List<Class<*>>? {
        return null
    }
}