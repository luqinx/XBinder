package com.luqinx.xbinder

import com.luqinx.xbinder.annotation.InvokeType

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal class NewCallbackOptions<T>(
    callbackClass: Class<T>,
    callbackProcess: String,
    val instanceId: String,
): NewServiceOptions<T>(callbackClass, callbackProcess) {
    init {
        isCallback = true
        invokeType = InvokeType.REMOTE_ONLY
    }
}