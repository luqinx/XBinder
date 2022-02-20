package com.luqinx.xbinder.sample.async.impl

import com.luqinx.xbinder.sample.async.AsyncCallService

/**
 * @author  qinchao
 *
 * @since 2022/2/19
 */
class AsyncCallServiceImpl: AsyncCallService {
    override fun asyncCall() {
        Thread.sleep(10)
        println("async call invoked in thread(${Thread.currentThread().name})")
    }

    override fun onewayCall() {
        println("oneway call started!!")
        Thread.sleep(3000)
        println("oneway call invoked in thread(${Thread.currentThread().name})")
    }

    override fun normalCall() {
        println("normal call started!!")
        Thread.sleep(1000)
        println("normal call invoked in thread(${Thread.currentThread().name})\n\n")
    }
}