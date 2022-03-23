package com.luqinx.xbinder.sample.callback

/**
 * @author  qinchao
 *
 * @since 2022/3/23
 */
interface SimpleCallbackService {
    fun invokeCallback(callback: Callback?)
    fun invokeSimpleCallback(callback: SimpleCallback)
}