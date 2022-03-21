package com.luqinx.xbinder.sample.callback

import chao.java.tools.servicepool.annotation.Service

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

    override fun unregisterCallback(callback: Callback) {
        callbacks.remove(callback)
    }

    override fun invokeCallback(callback: Callback) {
        println("invoke remote callback $callback")
        callback.onCallback()
    }


}