package com.luqinx.xbinder.sample

import android.content.Context
import chao.android.tools.servicepool.Spa
import chao.java.tools.servicepool.IService
import com.luqinx.xbinder.*
import com.luqinx.xbinder.sample.async.AsyncCallJavaService
import com.luqinx.xbinder.sample.async.AsyncCallService
import com.luqinx.xbinder.sample.async.impl.AsyncCallJavaServiceImpl
import com.luqinx.xbinder.sample.async.impl.AsyncCallServiceImpl
import com.luqinx.xbinder.sample.callback.LightBinderCallbackService
import com.luqinx.xbinder.sample.callback.impl.LightBinderCallbackServiceImpl
import com.luqinx.xbinder.sample.callback.SimpleCallbackService
import com.luqinx.xbinder.sample.callback.impl.SimpleCallbackServiceImpl
import com.luqinx.xbinder.sample.invoketype.RemoteOnlyService
import com.luqinx.xbinder.sample.invoketype.impl.RemoteOnlyServiceImpl
import com.luqinx.xbinder.sample.simple.*
import com.luqinx.xbinder.sample.simple.impl.*

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
            ): Any {
                return when (clazz) {
                    PrimitiveTypeService::class.java -> PrimitiveTypeServiceImpl()
                    PrimitiveArrayTypeService::class.java -> PrimitiveArrayTypeServiceImpl()
                    PrimitiveBoxArrayTypeService::class.java -> PrimitiveBoxArrayTypeServiceImpl()
                    ParcelableTypeService::class.java -> ParcelableTypeServiceImpl()
                    SerializableTypeService::class.java -> SerializableTypeServiceImpl()

                    AsyncCallService::class.java -> AsyncCallServiceImpl()
                    AsyncCallJavaService::class.java -> AsyncCallJavaServiceImpl()
                    BinderTypeService::class.java -> BinderTypeServiceImpl()

                    LightBinderCallbackService::class.java -> LightBinderCallbackServiceImpl()
                    SimpleCallbackService::class.java -> SimpleCallbackServiceImpl()

                    // invokeType
                    RemoteOnlyService::class.java -> RemoteOnlyServiceImpl()
                    else -> {
                        Spa.getService(clazz as Class<IService>) as ILightBinder
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