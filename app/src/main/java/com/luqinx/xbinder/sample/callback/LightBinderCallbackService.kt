package com.luqinx.xbinder.sample.callback

import android.view.View
import chao.java.tools.servicepool.IService
import com.luqinx.xbinder.ILightBinder

/**
 * @author  qinchao
 *
 * @since 2022/3/20
 */
interface LightBinderCallbackService: ILightBinder {
    fun registerCallback(callback: Callback)
    fun unregisterCallback(callback: Callback)

    fun invokeCallback(callback: Callback?)
    fun invokeCallbacks(callbacks: Array<Callback>)
    fun invokeObjects(objs: Array<Any?>)

    fun remoteGc()

    fun invokeClickListener(listener: View.OnClickListener)
}
