package com.luqinx.xbinder.sample.callback

import chao.java.tools.servicepool.IService
import com.luqinx.xbinder.IBinderService

/**
 * @author  qinchao
 *
 * @since 2022/3/20
 */
interface CallbackService: IBinderService,IService {
    fun registerCallback(callback: Callback)
    fun unregisterCallback(callback: Callback)

    fun invokeCallback(callback: Callback)
}