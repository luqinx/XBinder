package com.luqinx.xbinder.sample.callback.impl

import com.luqinx.xbinder.sample.callback.Callback
import com.luqinx.xbinder.sample.callback.SimpleCallback
import com.luqinx.xbinder.sample.callback.SimpleCallbackService

/**
 * @author  qinchao
 *
 * @since 2022/3/23
 */
class SimpleCallbackServiceImpl: SimpleCallbackService {
    override fun invokeCallback(callback: Callback?) {
        println("simple invokeCallback invoked! ")
        callback?.onCallback()
    }

    override fun invokeSimpleCallback(callback: SimpleCallback) {
        println("simple invokeClickListener invoked! ")
        callback.onCallback()
    }
}