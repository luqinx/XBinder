package com.luqinx.xbinder.sample.callback.impl

import android.view.View
import chao.java.tools.servicepool.annotation.Service
import com.luqinx.xbinder.sample.callback.Callback
import com.luqinx.xbinder.sample.callback.LightBinderCallbackService
import com.luqinx.xbinder.sample.callback.SimpleCallback

/**
 * @author  qinchao
 *
 * @since 2022/3/20
 */
@Service
class LightBinderCallbackServiceImpl: LightBinderCallbackService {
    private val callbacks = arrayListOf<Callback>()

    override fun registerCallback(callback: Callback) {
        callbacks.add(callback)
    }

    override fun invokeCallbacks(callbacks: Array<Callback>) {
        callbacks.forEach {
            it.onCallback()
        }
    }

    override fun invokeObjects(objs: Array<Any?>) {
        objs.forEach {
            if (it is Callback) {
                it.onCallback()
            }
        }
        println("invokeObjects remote: ${objs.contentToString()}")
    }

    override fun remoteGc() {
        System.gc()
        System.runFinalization()
        System.gc()
    }

    override fun invokeClickListener(listener: View.OnClickListener) {
        println("invokeClickListener invoked")
        listener.onClick(null)
    }

    override fun getLightBinder(): Callback {
        return object: Callback{
            override fun onCallback() {
                println("LightBinder return onCallback invoked!!")
            }

        }
    }

    override fun getSimpleLightBinder(): SimpleCallback {
        return object: SimpleCallback{
            override fun onCallback() {
                println("SimpleCallback return onCallback invoked!!")
            }

        }
    }

    override fun unregisterCallback(callback: Callback) {
        callbacks.remove(callback)
    }

    override fun invokeCallback(callback: Callback?) {
        println("invoke remote callback $callback")
        callback?.onCallback()
    }


}