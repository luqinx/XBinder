package com.luqinx.xbinder.sample.async

import com.luqinx.xbinder.IBinderService
import com.luqinx.xbinder.annotation.AsyncCall
import com.luqinx.xbinder.annotation.OnewayCall

/**
 * @author  qinchao
 *
 * @since 2022/2/19
 */
interface AsyncCallService : IBinderService {

    @AsyncCall
    fun asyncCall()

    @OnewayCall
    fun onewayCall()

    fun normalCall()
}