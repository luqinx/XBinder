package com.luqinx.xbinder.sample.callback

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