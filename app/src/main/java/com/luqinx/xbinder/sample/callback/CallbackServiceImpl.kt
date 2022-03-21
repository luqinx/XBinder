package com.luqinx.xbinder.sample.callback

import chao.java.tools.servicepool.annotation.Service
import com.luqinx.xbinder.IBinderService

/**
 * @author  qinchao
 *
 * @since 2022/3/20
 */
@Service
class CallbackServiceImpl: CallbackService {
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

    override fun unregisterCallback(callback: Callback) {
        callbacks.remove(callback)
    }

    override fun invokeCallback(callback: Callback?) {
        println("invoke remote callback $callback")
        callback?.onCallback()
    }


}