package com.luqinx.xbinder.sample.simple

import android.os.IBinder
import com.luqinx.xbinder.sample.aidl.Oneway

/**
 * @author  qinchao
 *
 * @since 2022/2/19
 */
class BinderTypeServiceImpl: BinderTypeService {
    override fun getOnewayBinder(): IBinder {
        return onewayService
    }

    private val onewayService = OnewayService()

    inner class OnewayService: Oneway.Stub() {
        override fun onewayCall() {
            println("oneway service oneway call start")
        Thread.sleep(5000)
            println("oneway service oneway call end!!")

        }

        override fun normalCall() {
            println("oneway service normal call start")
            Thread.sleep(5000)
            println("oneway service normal call end!! ${Thread.currentThread().name}")

        }
    }
}